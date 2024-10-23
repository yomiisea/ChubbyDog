package com.mygdx.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mygdx.game.items.Arma;
import com.mygdx.game.items.Armadura;
import com.mygdx.game.items.Carta;
import com.mygdx.game.items.Item;
import com.mygdx.game.items.Llave;
import com.mygdx.game.items.Pocion;

public class Inventario implements ArchivoInv{
    private List<Item> items;

    public Inventario() {
        items = new ArrayList<>();
    }

    public void addItem(String attributeString) {
        System.out.println(attributeString);
        String[] attributes = attributeString.split(",");
        System.out.println(attributes);
        // Obtener los atributos personalizados del rectángulo en el orden necesario
        int code = Integer.parseInt(attributes[0]);
        String nombre = attributes[5];
        String descripcion = attributes[1];
        String rareza = attributes[2];
        int value = Integer.parseInt(attributes[3]);
        int durabilidad = Integer.parseInt(attributes[4]);
        boolean obtenido = Boolean.parseBoolean(attributes[6]);

        Item item = null;

        // Determinar el tipo de item según los atributos proporcionados
        if (attributes.length > 7) {
            if (code == 1) {
                if (attributes.length >= 9) {
                    int idLlave = Integer.parseInt(attributes[7]);
                    int idPuerta = Integer.parseInt(attributes[8]);

                    item = new Llave(code, nombre, descripcion, rareza, value, durabilidad, obtenido, idLlave, idPuerta);
                }
            } else if (code == 2) {
                if (attributes.length == 9) {
                    int danio = Integer.parseInt(attributes[7]);
                    item = new Arma(code, nombre, descripcion, rareza, value, durabilidad, obtenido, danio);
                }
            } else if (code == 3) {
                if (attributes.length >= 9) {
                    int proteccion = Integer.parseInt(attributes[7]);
                    item = new Armadura(code, nombre, descripcion, rareza, value, durabilidad, obtenido, proteccion);
                }
            } else if (code == 4) {
                if (attributes.length >= 9) {
                    int aumentoPoder = Integer.parseInt(attributes[7]);
                    item = new Pocion(code, nombre, descripcion, rareza, value, durabilidad, obtenido, aumentoPoder);
                }
            } else if (code == 5) {
                if (attributes.length >= 9) {
                    String contenido = attributes[7];
                    item = new Carta(code, nombre, descripcion, rareza, value, durabilidad, obtenido, contenido);
                }
            }
        }

        // Agregar el item a la lista de items del inventario
        if (item != null) {
            items.add(item);
            guardarInventario("inventario.txt" );
        }
    }
    

    public void guardarInventario(String nombreArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ArchivoInv.TXT_INVENTARIO, true))) {
            for (Item item : items) {
                writer.write(item.toString());
                writer.newLine();
            }
            System.out.println(items);
            System.out.println("Inventario guardado exitosamente en el archivo: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error al guardar el inventario en el archivo: " + nombreArchivo);
            e.printStackTrace();
        }
    }

public static ArrayList<Item>leerInventario() {
    ArrayList<Item> inventario = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(ArchivoInv.TXT_INVENTARIO))) {
        String linea;
        while ((linea = reader.readLine()) != null) {
            String[] atributos = linea.split("%");
            if (atributos.length >= 6) {
            	int code = Integer.parseInt(atributos[0]);
                String nombre = atributos[1];
                String descripcion = atributos[2];
                String rareza = atributos[3];
                int value = Integer.parseInt(atributos[4]);
                int durabilidad = Integer.parseInt(atributos[5]);
                boolean obtenido = Boolean.parseBoolean(atributos[6]);

                Item item = null;

                if (code==1) {
                    if (atributos.length >= 6) {           
                        int idLlave = Integer.parseInt(atributos[7]);
                        int idPuerta = Integer.parseInt(atributos[8]);
                        item = new Llave(code, nombre, descripcion, rareza, value, durabilidad, obtenido, idLlave, idPuerta);
                    }
                } else if (atributos[0].equals("Arma")) {
                    if (atributos.length >= 9) {
                        int danio = Integer.parseInt(atributos[8]);
                        item = new Arma(value, descripcion, rareza, nombre, durabilidad, code, obtenido, danio);
                    }
                } else if (atributos[0].equals("Armadura")) {
                    if (atributos.length >= 9) {
                        int proteccion = Integer.parseInt(atributos[8]);
                        item = new Armadura(value, descripcion, rareza, nombre, durabilidad, code, obtenido, proteccion);
                    }
                } else if (atributos[0].equals("Carta")) {
                    if (atributos.length >= 9) {
                        String contenido = atributos[8];
                        item = new Carta(value, descripcion, rareza, nombre, durabilidad, code, obtenido, contenido);
                    }
                } else if (atributos[0].equals("Pocion")) {
                    if (atributos.length >= 9) {
                        int aumentoPoder = Integer.parseInt(atributos[8]);
                        item = new Pocion(value, descripcion, rareza, nombre, durabilidad, code, obtenido, aumentoPoder);
                    }
                }

                if (item != null) {
                    inventario.add(item);
                }
            }
        }
    } catch (IOException e) {
        System.err.println("Error al leer el inventario del archivo: ");
        e.printStackTrace();
    }

    return inventario;
	}
}