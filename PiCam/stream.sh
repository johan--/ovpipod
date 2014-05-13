#!/bin/bash

base="/var/www"

set -x

rm -rf live.*

# fifos seem to work more reliably than pipes - and the fact that the
# fifo can be named helps ffmpeg guess the format correctly.
mkfifo live.h264
raspivid -w 1280 -h 720 -fps 25 -hf -t 86400000 -b 1800000 -o - | psips > live.h264 &

# Letting the buffer fill a little seems to help ffmpeg to id the stream
sleep 2

# Need ffmpeg around 1.0.5 or later. The stock Debian ffmpeg won't work.
# I'm not aware of options apart from building it from source. I have
# Raspbian packags built from Debian Multimedia sources. Available on
# request but I don't want to post them publicly because I haven't cross
# compiled all of Debian Multimedia and conflicts can occur.
ffmpeg -y \
  -i live.h264 \
  -c:v copy \
  -map 0:0 \
  -f segment \
  -segment_time 8 \
  -segment_format mpegts \
  -segment_list "$base/live.m3u8" \
  -segment_list_size 720 \
  -segment_list_flags live \
  -segment_list_type m3u8 \
  -strict -2 "%08d.ts" < /dev/null 

rm live.m3u8 live/*.ts

# vim:ts=2:sw=2:sts=2:et:ft=sh
