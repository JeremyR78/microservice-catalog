package com.ecommerce.catalog.publisher.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TimerByCommand {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private   final Logger logger  = LoggerFactory.getLogger(getClass());
    private int         numberOfCommand;
    private long        timeInMillisecond;
    private List<Date>  counterDateList;

    private static final String SERVICE             = "[COMMAND]";
    private static final String SUB_SERVICE_EXECUTE = "[EXECUTE]";
    private static final String SUB_SERVICE_CHECK   = "[CHECK]";
    private static final String SUB_SERVICE_VALID   = "[VALID]";
    private static final String SUB_SERVICE_WAIT    = "[WAIT]";


    // --------------------------------------
    // -        Constructors                -
    // --------------------------------------

    /**
     *
     * @param numberOfCommand
     * @param timeInMillisecond
     */
    public TimerByCommand( int numberOfCommand, long timeInMillisecond )
    {
        this.counterDateList    = new ArrayList<>();
        this.numberOfCommand    = numberOfCommand;
        this.timeInMillisecond  = timeInMillisecond;
    }

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    /**
     * Ajout d'une nouvelle commande et valide le nombre
     *
     * @return
     */
    public boolean addNewCommandAndValid()
    {
        this.setNewCommand();
        this.check();
        return this.valid();
    }

    /**
     * Ajout d'une nouvelle commande et valide le nombre
     * Block le temps à attendre
     */
    public void checkCommandAndValidBlock()
    {
        this.check();
        if( ! this.valid() )
        {
            this.getWaitTime();
        }
    }

    /**
     * Incrémentation du compteur de commande
     *
     */
    public void setNewCommand()
    {
        // Nouvelle commande
        this.counterDateList.add( new Date() );
    }

    /**
     *  Suppresion des anciennes commandes
     */
    public void check()
    {
        Date obsoleteDate         = this.getObsoleteDate();
        List<Date> dateToRemove   = new ArrayList<>();

        // Vérification de la liste
        for( Date dateCommand : this.counterDateList )
        {
            if( obsoleteDate.after( dateCommand ) ) {
                dateToRemove.add( dateCommand );
            }
        }

        this.getLogger().debug("{}{} Date d'obsolescence : {} -> Date de commande(s) en amont à supprimer : {} . Toutes les autres dates : {} ",
                SERVICE, SUB_SERVICE_CHECK, obsoleteDate, dateToRemove, this.counterDateList );

        // Suppression des dates obsoletes
        this.counterDateList.removeAll( dateToRemove );
    }

    /**
     *
     * @return
     */
    public Date getFutureDate()
    {
        if( ! this.counterDateList.isEmpty() )
        {
            int size        = this.counterDateList.size();
            Date lastDate   = this.counterDateList.get( size - 1 );
            Calendar cal    = new GregorianCalendar();
            cal.setTime( lastDate );
            // Ajout du temps à attendre à la dernière date d'éxécution de commande
            cal.add( Calendar.MILLISECOND, (int) this.timeInMillisecond );
            return cal.getTime();
        }
        return new Date();
    }

    public Date getObsoleteDate()
    {
        Date now        = new Date();
        Calendar cal    = new GregorianCalendar();
        cal.setTime( now );
        // Supprime le temps max du compteur
        cal.add( Calendar.MILLISECOND, - (int) this.timeInMillisecond );
        return cal.getTime();
    }


    /**
     * Nombre de commande
     * @return
     */
    public int getSize( )
    {
        return this.counterDateList.size();
    }

    /**
     * Le nombre de commande est inférieur à celui autorisée
     *
     * @return
     */
    public boolean valid()
    {
        if( this.getSize() >= this.numberOfCommand )
        {
            this.getLogger().debug("{}{} Le nombre maximum de commande sur un temps donnée est atteind ! Nombre de commande : {} ! Nombre de commande autorisée {}  ",
                    SERVICE, SUB_SERVICE_VALID, this.getSize(), this.numberOfCommand);
            return false;
        }
        this.getLogger().debug("{}{} Nombre de commande : {} ! Nombre de commande autorisée {}  ",
                SERVICE, SUB_SERVICE_VALID, this.getSize(), this.numberOfCommand);
        return true;
    }

    /**
     * Attend le temps minimum entre la première commande et la date limite
     *
     */
    public void getWaitTime()
    {
        if( ! this.counterDateList.isEmpty() )
        {
            Date dateLimit = this.getFutureDate();
            Date now = this.counterDateList.get(0);

            long diffInMillies = Math.abs(  dateLimit.getTime() - now.getTime() );

            this.getLogger().debug( "{}{} Temps à attendre avant la prochaine commande : {} ms. Date actuelle : {} - Date limite : {}",
                    SERVICE, SUB_SERVICE_WAIT, diffInMillies, now, dateLimit );

            try {
                // Attend
                Thread.sleep( diffInMillies );
            }
            catch ( InterruptedException ex )
            {
                // Cancel
            }
        }
        else
        {

        }
    }


    /**
     * Le logger
     * @return
     */
    protected Logger getLogger()
    {
        return this.logger;
    }

}
