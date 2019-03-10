#!/bin/bash

. bin/activate

export PATH="~/usr/local/microbit/gcc-arm-none-eabi/bin:$PATH"

yt target bbc-microbit-classic-gcc

yt build
