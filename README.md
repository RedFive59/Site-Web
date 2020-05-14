# Prise en main de Spring et de MongoDB

## Site de partage de musique

Les musiques sont envoyées par les utilisateurs qu'ils partagent en public, celles-ci peuvent être liké ou disliké.
Les liens de musique sont des liens Spotify.
[Trello](https://trello.com/b/hE5KhqFs/projet-web)

## Installation
(J'ai fait le développement et les tests sur Windows 10 mais normalement il devrait fonctionner sur toutes les plateformes)

Pour faire fonctionner ce code il vous faut :

* [MongoDB Community Server](https://www.mongodb.com/download-center/community) pour créer la BDD et ainsi gérer les données
* [Robo3T](https://robomongo.org/download) pour afficher la BDD
* [Java JDK (1.8)](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html) pour lancer le .jar du serveur (en localhost)
* [L'archive master.zip](https://github.com/RedFive59/Site-Web/archive/master.zip) dézippé dans un dossier de votre ordinateur

Une fois que tout cela est installé on peut passer au lancement.

## Lancement
Pour lancer le serveur vous devez vérifier que votre base de données MongoDB est bien active.

Pour cela il vous suffit, après installation, de regarder vos services dans votre [Gestionnaire des tâches](https://lecrabeinfo.net/ouvrir-gestionnaire-des-taches-de-windows.html) si le service MongoDB est bien en cours d'exécution. Si ce n'est pas le cas lancer le.
Vous aurez alors ceci :
![Gestionnaire](https://i.imgur.com/bPJw56D.png "Gestionnaire des tâches")

Pour vérifier que votre base est bien accessible vous pouvez allumer Robo3T et vous connecter à l'adresse locahost:27017 :

![Robo3T](https://i.imgur.com/KkZU3Qg.png "Robo3T")

On arrive donc sur ce serveur :

![Robo3T Résultat](https://i.imgur.com/Yrxn4SR.png "Robo3T Résultat")

Si tout c'est bien passé, on peut alors lancer le serveur via le fichier 
```bash
launch.bat
```
Après le lancement de ce fichier vous aurez un terminal Windows que voici :
![Terminal](https://i.imgur.com/lofP7HG.png "Windows Terminal")

Le premier lancement peut durer entre 1 à 5 minutes selon votre connexion et selon la puissance de votre ordinateur. Il va télécharger les fichiers dont il a besoin pour lancer l'application

J'ai remarqué en faisant le test d'une installation sur un autre PC que l'application pouvait parfois se lancer quelques minutes après que la console ait fini ces opérations.

Si vous voyez bien cette page de terminal alors vous pouvez simplement ouvrir votre navigateur, et accéder à la page [localhost:8080](localhost:8080) 

## Aperçu
Voici un aperçu du résultat final :
![Resultat](https://i.imgur.com/XApzMnh.png "Affichage du site")

## Problèmes possibles
Normalement il n'y a pas d'erreur mais voici une erreur que j'ai pu voir auparavant:

Si votre page affiche une erreur et votre url ressemble à ça : 

```localhost:8080/?lang=?-----?``` avec des lettres à la place des tirets alors remplacer le lien par ```localhost:8080/?lang=FR``` et choisissez un pays en haut à droite. (Erreur de variable locale liée à Javascript)

## Outils utilisées
Pour editer mon code j'ai utilisé l'IDE [IntelliJ](https://www.jetbrains.com/fr-fr/idea/download/#section=windows) car il propose directement un terminal Windows implementé pour faire ses essais 

Pour avoir un affichage plus présentable j'ai choisi d'utiliser la charte [Shards.css](https://designrevision.com/downloads/shards/) qui est un kit UI pour Bootstrap.

J'ai également utilisé l'[API Spotify Web Java](https://github.com/thelinmichael/spotify-web-api-java) de [thelinmichael](https://github.com/thelinmichael) pour différentes opérations.

## Maquette de base du site
Voici la maquette qui a servi à la création des pages Web :
![Maquette](https://i.imgur.com/agWNRVd.png "Maquette du site")
