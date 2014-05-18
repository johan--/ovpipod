#!/bin/bash

cd bin_hexapod
# Copie des fichiers requis
cp ../hexapod/exe/hexapod.jar usr/local/bin/hexapod/
cp ../wwwRemote/gamepadapi.js var/www/
cp ../wwwRemote/gamepadapi.js var/www/
cp ../wwwRemote/gamepadapi.js var/www/

# Construction du paquet
sudo dpkg-deb --build hexapod.deb