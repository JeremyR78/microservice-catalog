package com.ecommerce.catalog.publisher.core;

import com.ecommerce.catalog.utils.logs.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class FifoController implements Runnable {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private String service                          = "FIFO_CONTROLLER";
    private String subService                       = "EXECUTE";
    private static final String RUN                 = "RUN";
    private static final String SUB_SERVICE_STOP    = "STOP";
    private static final String SUB_SERVICE_FAIL    = "FAIL";
    private static final int TIME_OUT_MAX_FIFO      = 500;

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private   final Logger logger                       = LoggerFactory.getLogger(getClass());
    protected final BlockingQueue< ? extends Callable<?> > fifo;
    protected final CopyOnWriteArrayList< ? extends Callable<?> > toCurrentObject;
    protected final ExecutorService executorService;

    protected volatile boolean running                  = false;
    protected volatile boolean stop                     = false;
    protected volatile boolean init                     = false;
    protected long maxTimeMillisByCommand               = 60 * 60 * 1000 ; // 1H00

    protected List<TimerByCommand> timerList;

    // --------------------------------------
    // -        Constructors                -
    // --------------------------------------

    /**
     *
     * @param fifo : La FIFO à lire
     * @param toCurrentObject : La liste de tous les objets qui sont actuellement dans la FIFO. Cela permet d'éviter de dupliquer ceux déjà existant.
     * @param maxPoolThread : Le nombre de commande à executer en paralléle
     */
    public FifoController( BlockingQueue< ? extends Callable<?> > fifo,
                           CopyOnWriteArrayList< ? extends Callable<?> > toCurrentObject,
                           int maxPoolThread )
    {
        this.fifo               = fifo;
        this.toCurrentObject    = toCurrentObject;
        this.executorService    = Executors.newFixedThreadPool(maxPoolThread);
        this.timerList          = new ArrayList<>();
    }

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Override
    public void run ()
    {
        running = true;
        try {
            while ( ! stop )
            {
                Future<?> future;
                // Récupération d'un élément
                Callable<?> command = fifo.poll( TIME_OUT_MAX_FIFO, TimeUnit.MILLISECONDS );
                // Suppression de la commande retirée de la FIFO
                this.toCurrentObject.remove( command );

                if ( command != null ) {
                    this.getLogger().debug("{}{}{} Excecution de la commande {}",
                            service, subService, RUN, command );
                    // Execute les commandes
                    future = executorService.submit( command );

                    for( TimerByCommand  timer : this.timerList )
                    {
                        // Incrémente les compteurs
                        timer.setNewCommand();
                    }

                    // Attend l'éxecution de la commande ( avec un temps maximum )
                    future.get( this.maxTimeMillisByCommand, TimeUnit.MILLISECONDS  );

                    if( ! future.isDone() )
                    {
                        // Annulation de la commande si celle-ci est trop longue
                        future.cancel( true );
                        this.getLogger().warn("{}{}{} Annulation de la commande : {} ! Durée max : {} {}",
                                service, subService, RUN, command, this.maxTimeMillisByCommand, TimeUnit.MILLISECONDS );
                    }
                }

                // Vérification des compteurs / timers
                for( TimerByCommand  timer : this.timerList )
                {
                    // Vérification du compteur et block si nécessaire
                    timer.checkCommandAndValidBlock();
                }

                if( fifo.isEmpty() )
                {
                    this.stop = true;
                    // Aucune autre tache ne peut être éxecutée
                    this.executorService.shutdown();
                    this.getLogger().info("{}{}{} La FIFO est vide : {} . Demande d'arrêt du service.",
                            service, subService, RUN, this.toCurrentObject );
                }

            }
        } catch ( InterruptedException e ) {
            int sizeCommand = fifo.size();
            this.getLogger().warn("{}{}{} Le controleur a été interrompu ! {} message(s) n'ont pas été envoyé(s) !",
                    service, subService, SUB_SERVICE_STOP, sizeCommand );
        } catch (Exception ex) {
            int sizeCommand = fifo.size();
            this.getLogger().error("{}{}{} Erreur durant l'éxecution du controleur ! {} message(s) n'ont pas été envoyé(s) ! MESSAGE : {} : {}",
                    service, subService, SUB_SERVICE_FAIL, sizeCommand, ex.getMessage(), ex.getStackTrace() );
        } finally {

            // Attendre les derniers threads
            if( ! this.executorService.isTerminated() ) {
                this.getLogger().info( LogUtil.format(service, subService, "Le contrôleur attend les derniers Threads",
                        String.format("Le contrôleur attend %s ms que les derniers Threads se terminent correctement",
                                TIME_OUT_MAX_FIFO )));
                try {
                    this.executorService.awaitTermination( TIME_OUT_MAX_FIFO, TimeUnit.MILLISECONDS );
                } catch (InterruptedException e) {
                    this.getLogger().warn(LogUtil.format(service, subService,
                            "Demande d'arrêt brutal du contrôleur !",
                            e));
                }
            }

            if( ! this.executorService.isTerminated() ){
                this.getLogger().warn(LogUtil.format(service, subService, "Certains Threads seront arrétés brutalement !"));
            }
            // Arrêt de tous les Threads et du service
            this.executorService.shutdownNow();

            running = false;
            stop    = false;

            this.getLogger().info("{}{}{} Le controleur est arrêté.",
                    service, subService, SUB_SERVICE_STOP);
        }
    }

    public void stopController()
    {
        this.stop = true;
        this.getLogger().info("{}{}{} Demande d'arrêt du controleur.",
                service, subService, SUB_SERVICE_STOP);
    }

    /**
     *
     * @return
     */
    public boolean isStopped()
    {
        return ! this.running;
    }

    /**
     * Ajout d'un compteur qui laissera passer un nombre de commande dans un temps donnée
     *
     * @param numberOfCommand : le nombre de commande
     * @param time            : le temps
     * @param timeUnit        : l'unité de temps
     */
    public void addCounter( int numberOfCommand, long time, TimeUnit timeUnit )
    {
        TimeUnit timeConvert    = TimeUnit.MILLISECONDS;
        long timer              = timeConvert.convert( time, timeUnit );
        this.timerList.add( new TimerByCommand( numberOfCommand, timer ));
    }

    /**
     *
     * @param timerCommand
     */
    public void addCounter( TimerCommand timerCommand)
    {
        this.addCounter( timerCommand.getNumberOfCommand(),
                timerCommand.getUnitTime(),
                timerCommand.getTimeConvert() );
    }

    public void setMaxTimerCommand( long time, TimeUnit timeUnit )
    {
        TimeUnit timeConvert    = TimeUnit.MILLISECONDS;
        this.maxTimeMillisByCommand = timeConvert.convert( time, timeUnit );
    }

    public void setMaxTimerCommand( TimeOutCommand timeOutCommand )
    {
        this.setMaxTimerCommand( timeOutCommand.getUnitTime(),
                timeOutCommand.getTimeConvert() );
    }

    /**
     * Le logger
     * @return
     */
    protected Logger getLogger()
    {
        return this.logger;
    }


    public void setLoggerServiceName( String serviceName ){
        service = LogUtil.formatName( serviceName );
    }

    public void setLoggerSubServiceName( String subServiceName ){
        subService = LogUtil.formatName( subServiceName );
    }

}
