clang `pkg-config --cflags gtk+-3.0` -O2 -o BTME.bin main.c -lm `pkg-config --libs gtk+-3.0` -rdynamic
./BTME.bin
