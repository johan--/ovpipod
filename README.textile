OVPiPod
=======

Programme hexapod TrossenRobotics

!http://www.overware.fr/images/mecanique/PhantomX_AX_Mark_II/PhantomX_AX_Hexapod_Mark_II.jpg!

h2. Installation

L'installation peut être effectué sur un Raspberry Pi faisant tourner Raspbian.

h3. Paramétrage de l'interface série

Plusieurs fichiers sont à modifier.

Par defaut, l'horloge de l'uart du Raspberry Pi est initialisé à 3000000 (3MHz), ce qui
est insuffisant pour contrôler les servomoteurs Dynamixels.

Il faut donc l'initialiser à 16000000 (16MHz) en rajoutant dans le fichier `/boot/config.txt`

@init_uart_clock=16000000@

Ensuite il faut retirer toutes les options mentionnant ttyAMA0 du fichier `/boot/cmdline.txt`,
pour ne pas initialiser la mauvaise vitesse.

exemple :

Ceci :
@dwc_otg.lpm_enable=0 console=ttyAMA0,115200 kgdboc=ttyAMA0,115200 console=tty1 root=/dev/mmcblk0p2 rootfstype=ext4 elevator=deadline rootwait@

Devient:
@dwc_otg.lpm_enable=0 console=tty1 root=/dev/mmcblk0p2 rootfstype=ext4 elevator=deadline rootwait@

Et enfin il faut éditer le fichier `/etc/inittab`, pour aussi enlever les référence à ttyAMA0.
Supprimez ou commentez la ligne suivante :

@T0:23:respawn:/sbin/getty -L ttyAMA0 115200 vt100@

h3. Installation du programme

Le programme java necessite une machine JDK1.7.

Ce programe se trouve dans le dossier `hexapod/exe/hexapod.jar`

Le programme client des WebSockets se trouve dans le dossier `wwwRemote/index.html`
Ce fichier sert à connecter les gamepads, ou à afficher l'interface de controle pour l'hexapod.


h2. changelog:

v1 : version fonctionnelle du programme de controle
