#include <stdlib.h>
#include <sys/types.h>
#include <signal.h>
#include <unistd.h>
#include <string.h>
#include <gtk/gtk.h>
#include <gtk/gtkx.h>
#include <math.h>
#include <ctype.h>

GtkWidget *gtk_window1;
GtkWidget *gtk_fixed1;
GtkWidget *gtk_menu;
GtkWidget *gtk_menu_file;
GtkWidget *gtk_menu_help;
GtkWidget *gtk_help_window;

GtkBuilder *builder;

int main(int argc, char *argv[])
{
  gtk_init(&argc, &argv);

  builder = gtk_builder_new_from_file("editor.glade");
  gtk_window1 = GTK_WIDGET(gtk_builder_get_object(builder, "gtk_window1"));

  g_signal_connect(gtk_window1, "destroy", G_CALLBACK(gtk_main_quit), NULL);
  gtk_builder_connect_signals(builder, "gtk_window1");
  
  gtk_fixed1 = GTK_WIDGET(gtk_builder_get_object(builder, "gtk_fixed1"));
  gtk_menu = GTK_WIDGET(gtk_builder_get_object(builder, "gtk_menu"));
  gtk_menu_file = GTK_WIDGET(gtk_builder_get_object(builder, "gtk_menu_file"));
  gtk_menu_help = GTK_WIDGET(gtk_builder_get_object(builder, "gtk_menu_help"));
  gtk_help_window = GTK_WIDGET(gtk_builder_get_object(builder, "gtk_help_window"));

  gtk_widget_show(gtk_window1);
  gtk_main();

  return EXIT_SUCCESS;

}

void on_menu_help_select(GtkButton *b)
{
  gtk_widget_show(gtk_help_window);
}
