package com.ecommerce.catalog.publisher.core;


import com.ecommerce.catalog.utils.logs.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

import static com.ecommerce.catalog.utils.logs.LogNameSpace.*;


public abstract class AsyncService implements Serializable {

    private String serviceName                       = "ASYNC_SERVICE";
    private String subServiceName                    = "EXECUTOR";
    private static final int NUMBER_OF_CONTROLLER    = 1;
    private final Integer numberMaxPoolThread;
    private static final int NUMBER_MAX_ELEMENT_IN_FIFO = 1000;

    private   final Logger logger  = LoggerFactory.getLogger(getClass());

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private transient final ExecutorService executorController                    = Executors.newFixedThreadPool(NUMBER_OF_CONTROLLER);
    private transient final BlockingQueue<Callable<?>> fifo                       = new ArrayBlockingQueue<>(NUMBER_MAX_ELEMENT_IN_FIFO);
    private transient final CopyOnWriteArrayList<Callable<?>> toAnalyseCommand    = new CopyOnWriteArrayList<>();

    private transient FifoController fifoController;
    private boolean running;

    private transient final List<TimerCommand> counterList;
    private transient final TimeOutCommand timeOutCommand;

    // --------------------------------------
    // -        Attributes  - SERVICE       -
    // --------------------------------------


    // --------------------------------------
    // -        Constructors                -
    // --------------------------------------

    /**
     *
     * @param timerCommandList : La liste des timers à respecter
     */
    public AsyncService( List<TimerCommand> timerCommandList )
    {
        this( 5, timerCommandList, null);
    }

    /**
     *
     * @param maxThread : Le nombre de thread éxecuté en parallèle
     * @param timerCommandList : La liste des timers à respecter
     * @param timeOutCommand : Le temps maximum d'éxecution d'une commande
     */
    public AsyncService( int maxThread, List<TimerCommand> timerCommandList, TimeOutCommand timeOutCommand )
    {
        this.numberMaxPoolThread = maxThread;
        this.timeOutCommand = Objects.requireNonNullElseGet(timeOutCommand, () -> new TimeOutCommand(5, TimeUnit.MINUTES));
        this.counterList = Objects.requireNonNullElseGet(timerCommandList, ArrayList::new);
    }

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    /**
     *
     * @param command : La commande à ajouter à la FIFO
     * @return Faux si la commande est déjà dans la Fifo sinon Vrai
     * @throws IllegalStateException : La fifo est pleine
     */
    public boolean addCommand( Callable<?> command ) throws IllegalStateException
    {
        // Vérifie si la commande est déjà dans la FIFO
        if( this.isInFifo( command ) )
        {
            this.logger.debug("{}{}{} La commande ({}) est déjà dans la FIFO (total : {}) : {}",
                    fLog(serviceName), fLog(subServiceName), fLog(ADD),
                    command, this.toAnalyseCommand.size(), this.toAnalyseCommand);
            return false;
        }

        // Ajout de l'identifiant uniquement
        this.fifo.add( command );
        // La liste sert à visuliser les commandes dèjà dans la FIFO
        this.toAnalyseCommand.add( command );


        this.logger.debug("{}{}{} La liste de commande dans la FIFO (total : {}) : {}",
                fLog(serviceName), fLog(subServiceName), fLog(ADD),
                this.toAnalyseCommand.size(), this.toAnalyseCommand);
        return true;
    }


    /**
     * Démarrare le contrôleur d'anlayse des commandes
     */
    @Async
    public void executorAsynchronously()
    {
        if( ! this.isStopped() || this.running )
        {
            return;
        }

        this.running = true ;

        this.logger.info("{}{}{} Démarrage du scheduler",
                fLog(serviceName), fLog(subServiceName), fLog(START) );

        this.fifoController = new FifoController( this.fifo, this.toAnalyseCommand, numberMaxPoolThread);
        this.fifoController.setLoggerServiceName( this.serviceName);
        this.fifoController.setLoggerSubServiceName( this.subServiceName);

        // Ajout des Timers
        for( TimerCommand timerCommand : this.counterList )
        {
            this.fifoController.addCounter(timerCommand);
        }

        // Temps maximun d'une exécution de commande
        this.fifoController.setMaxTimerCommand( this.timeOutCommand );

        Future future = this.executorController.submit( this.fifoController );

        try {
            future.get();
        }
        catch(InterruptedException | ExecutionException ie)
        {
            this.logger.warn("{}{}{} Intérruption du contrôleur ! Exception : {} : {} ",
                    fLog(serviceName), fLog(subServiceName), fLog(STOP), ie.toString(), ie.getStackTrace() );
        }
        finally
        {
            if( ! future.isDone() ){
                this.logger.warn(LogUtil.format( serviceName, subServiceName, STOP,
                        "Le contrôleur de FIFO va être arrêté !", ""));
                future.cancel( true );
            }

            this.running = false;
            this.logger.info("{}{}{} Arrêt du scheduler",
                    fLog(serviceName), fLog(subServiceName), fLog(STOP));
        }
    }

    /**
     * Demande l'arrêt du contrôleur
     */
    public void stop()
    {
        if( this.fifoController == null ) {
            return;
        }
        this.fifoController.stopController();
    }

    /**
     *
     * @return
     */
    public boolean isStopped()
    {
        if( this.fifoController == null ) {
            return true;
        }
        return this.fifoController.isStopped();
    }


    /**
     * Vérifie si la commande a déjà été mise dans la FIFO
     *
     * @param command : La commande à vérifier
     * @return
     */
    public boolean isInFifo( Callable command )
    {
        for( Callable commandToAnalyse : this.toAnalyseCommand )
        {
            if( Objects.equals( commandToAnalyse, command ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return Le nombre de commande dans la FIFO
     */
    public int getSizeFifo()
    {
        return this.fifo.size();
    }

    /**
     *
     * @return Le nombre maximun d'element à mettre dans la FIFO
     */
    public int getMaxSizeFifo()
    {
        return NUMBER_MAX_ELEMENT_IN_FIFO;
    }

    public List<TimerCommand> getCounterList() {
        return new ArrayList(counterList);
    }

    /**
     *
     * @return Affiche un snapshot des commandes qui seront exécutées par le contrôleur
     */
    public List<Callable<?>> getAllCommands()
    {
        return new ArrayList<>( this.toAnalyseCommand );
    }

    public void setLoggerServiceName( String serviceName ){
        this.serviceName = LogUtil.formatName( serviceName );
    }

    public void setLoggerSubServiceName( String subServiceName ){
        this.subServiceName = LogUtil.formatName( subServiceName );
    }

}
