# Prise en main de Spring et de MongoDB

## Site de partage de musique

Les musiques sont envoyées par les utilisateurs qu'ils partagent en public, celles-ci peuvent être liké ou disliké.
Les liens de musique sont des liens Spotify.

## Installation
Pour faire fonctionner ce code il vous faut :

* Windows 10
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

Si vous voyez bien cette page de terminal alors vous pouvez simplement ouvrir votre navigateur, et accéder à la page [localhost:8080](localhost:8080)

## Aperçu
Voici un aperçu du résultat final :
![Resultat](https://i.imgur.com/XApzMnh.png "Affichage du site")

## Outils utilisées
Pour editer mon code j'ai utilisé l'IDE [IntelliJ](https://www.jetbrains.com/fr-fr/idea/download/#section=windows) car il propose directement un terminal Windows implementé pour faire ses essais 

## Maquette de base du site
Voici la maquette qui a servi à la création des pages Web :
![Maquette](https://i.imgur.com/agWNRVd.png "Maquette du site")
