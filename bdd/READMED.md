# Installation

## Démarrer 

Exécuter la commande `./start.sh`. Le mot de passe et login sont compris dans le script.

## Changer le mot de passe

Après une changement de mot de passe il est nécessaire de supprimer le volume de la base et de le recréer.

Pour le supprimer le volume associé au docker compose, exécuter la commande suivante `docker-compose -rm -v`

Vérifier que le volume est bien supprimer `docker volume ls`.
Si ce n'est pas le cas `docker volume rm -f bdd_catalog-db`.


# DBeaver

## En local 

Ajouter `AllowPublicKeyRetrieval=True` à la configuration pour demander automatiquement une clé public au serveur. ( à désactiver en PROD )


