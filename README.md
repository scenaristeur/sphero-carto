sphero-carto
============

Utilisation de Sphero http://www.gosphero.com/fr/ pour cartographier un environnement,
 une maison, une pièce.
Sphero-carto est un composant du projet http://smag0.blogspot.fr/

Sphero-carto pour android

Sphero-carto est en deux parties :
- UISmag --> l'application pour controler Sphero --> créé une application AsmagSphero.apk 
Connectez votre sphero en bluetooth.

utilisable en manuel ou en cliquant sur le bouton "Auto" , le bouton stop arrête le mode automatique.
 Lorsque l'on clique sur le bonton "Sleep" un fichier "smag-sphero-last.txt" est généré sur la carte sd dans le répertoire "smag-sphero"

 - SmagSpheroCarto  : à compiler avec APDE : https://play.google.com/store/apps/details?id=com.calsignlabs.apde
(ne pas oublier d'ajouter les permissions ( read_external_storage dans "Sketch Properties" / "Sketch Permissions"
--> créé une application SmagSpheroCarto qui permet de visualiser la trace ds Sphero
 
TODO :
 - gérer la mise en veille du téléphone
 - corriger les erreurs de positions suite aux collisions