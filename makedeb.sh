#!/bin/bash

# Creation des dossiers necessaires
mkdir bin_hexapod/var/
mkdir bin_hexapod/var/www/

# Copie des fichiers requis
cp hexapod/exe/hexapod.jar bin_hexapod/usr/local/bin/hexapod/
cp wwwRemote/gamepadapi.js bin_hexapod/var/www/
cp wwwRemote/index.html bin_hexapod/var/www/
cp wwwRemote/virtualjoystick.js bin_hexapod/var/www/

# Construction du paquet
sudo dpkg-deb --build bin_hexapod

# Finalisation
mv bin_hexapod.deb hexapod.deb
chmod +x hexapod.deb
