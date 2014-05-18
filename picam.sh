#!/bin/bash

raspivid -n -w 1280 -h 720 -b 4000000 -fps 20 -vf -hf -t 0 -o - | cvlc -vvv stream:///dev/stdin --sout '#rtp{sdp=rtsp://:9000/}' :demux=h264