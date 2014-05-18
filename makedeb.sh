#!/bin/bash

cd bin_hexapod
cp ../hexapod/exe/hexapod.jar usr/local/bin/hexapod/
sudo dpkg-deb --build hexapod.deb