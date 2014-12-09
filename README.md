sphero-carto
============
version en français plus bas ;-)
English : 
Using Sphero http://www.gosphero.com/fr/ to map an environment, a house, a room. Sphero-carto is a component of the project http://smag0.blogspot.fr/

Sphero-carto for android

Sphero-carto is in two parts:

UISmag -> application to control Sphero -> created a AsmagSphero.apk implementation https://github.com/scenaristeur/sphero-carto/blob/master/UiSmag/bin/UiSmag.apk Connect your sphero bluetooth.
used manually or by clicking the "Auto" button, the stop button stops the automatic mode. When you click on the bonton "Sleep" a "smag-sphero-last.txt" file is generated on the SD card in the "sphero-smag"

SmagSpheroCarto: to compile with APDE: https://play.google.com/store/apps/details?id=com.calsignlabs.apde (do not forget to add permissions (read_external_storage in "Sketch Properties" / "Sketch permissions "-> SmagSpheroCarto created an application that allows you to view the trace ds Sphero

TODO:
manage standby phone
correct errors on positions collision

----------------------------------------------------------------------------------
Francais : 

Utilisation de Sphero http://www.gosphero.com/fr/ pour cartographier un environnement,
 une maison, une pièce.
Sphero-carto est un composant du projet http://smag0.blogspot.fr/

Sphero-carto pour android

Sphero-carto est en deux parties :
- UISmag --> l'application pour controler Sphero --> créé une application AsmagSphero.apk https://github.com/scenaristeur/sphero-carto/blob/master/UiSmag/bin/UiSmag.apk
Connectez votre sphero en bluetooth.

utilisable en manuel ou en cliquant sur le bouton "Auto" , le bouton stop arrête le mode automatique.
 Lorsque l'on clique sur le bonton "Sleep" un fichier "smag-sphero-last.txt" est généré sur la carte sd dans le répertoire "smag-sphero"

 - SmagSpheroCarto  : à compiler avec APDE : https://play.google.com/store/apps/details?id=com.calsignlabs.apde
(ne pas oublier d'ajouter les permissions ( read_external_storage dans "Sketch Properties" / "Sketch Permissions"
--> créé une application SmagSpheroCarto qui permet de visualiser la trace ds Sphero
 
TODO :
 - gérer la mise en veille du téléphone
 - corriger les erreurs de positions suite aux collisions