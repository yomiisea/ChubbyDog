package com.mygdx.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ArchivoInv;
import com.mygdx.game.Inventario;
import com.mygdx.game.items.Item;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
public class EscapeRoom extends ApplicationAdapter {
    private static final int TILE_WIDTH = 16;
    private static final int TILE_HEIGHT = 16;
    private static final float PLAYER_SCALE = 0.2f;
    private static final float ZOOM_SCALE = 4.0f;

    private SpriteBatch batch;
    private SpriteBatch batch2;
    private SpriteBatch batch3;
    private Animation<TextureRegion> walkAnimation;
    private Texture idleTexture;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private Vector2 playerPosition;
    private Vector2 previousPlayerPosition;
    private Rectangle playerRect;
    private float stateTime;
    private float mapScale;
    private BitmapFont font;
    float fontSize = 8;
    float fontSize2 = 7;
    private List<String> monsterDialogLines;
    private int currentDialogLine;
    private boolean isShowingDialog;
    private boolean showinput;
    private TextureRegion[] monsterFrames;
    private Animation<TextureRegion> monsterAnimation;
    private Vector2 monsterPosition;
    
    //habla monstruos control
    private boolean habla1Lucius=false;
    private boolean habla2Lucius=false;
    private boolean resplucius;
    private boolean respdada=false;
    
    private boolean habla1Chef=false;
    private boolean habla2Chef=false;
    private boolean respchef;
    private boolean respdadachef=false;
    private boolean hasPocion=false;
    private boolean habla1Sennor=false;
    
    private boolean habla1Bruja=false;
    private boolean habla1Erebos=false;
    private TextureRegion[] monsterFrames2;
    private Animation<TextureRegion> monsterAnimation2;
    private Vector2 monsterPosition2;
    
    private TextureRegion[] monsterFrames3;
    private Animation<TextureRegion> monsterAnimation3;
    private Vector2 monsterPosition3;
    
    private TextureRegion[] monsterFrames4;
    private Animation<TextureRegion> monsterAnimation4;
    private Vector2 monsterPosition4;
    
    private TextureRegion[] monsterFrames5;
    private Animation<TextureRegion> monsterAnimation5;
    private Vector2 monsterPosition5;
    
    private BitmapFont dialogFont;    
    private Texture blankTexture;
    private String inputText;
    private List<List<String>> splitDialogues; 
    private void createPlayer() {
        MapLayer playerLayer = map.getLayers().get("Jugador");

        if (playerLayer != null) {
            for (MapObject object : playerLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle rectangle = rectangleObject.getRectangle();

                    // Leer las propiedades personalizadas "initialX" e "initialY"
                    String initialXString = rectangleObject.getProperties().get("initialX", String.class);
                    String initialYString = rectangleObject.getProperties().get("initialY", String.class);

                    // Convertir las coordenadas iniciales a números enteros
                    int initialX = Integer.parseInt(initialXString);
                    int initialY = Integer.parseInt(initialYString);

                    // Ajustar las coordenadas según la escala del mapa
                    initialX *= mapScale;
                    initialY *= mapScale;

                    // Establecer la posición inicial del jugador
                    playerPosition.set(initialX, initialY);
                    previousPlayerPosition.set(playerPosition);
                    playerRect.setPosition(playerPosition);
                }
            }
        }
    }

    List<List<String>> separatedDialogs2;
    List<List<String>> separatedDialogs;
    boolean tirada=true;
    
    boolean Luciusvivo=true;
    boolean Chefvivo=true;
    public void create() {
        batch = new SpriteBatch();
        batch2 = new SpriteBatch();
        batch3 = new SpriteBatch();
        font = new BitmapFont();
        
        font.getData().setScale(fontSize / font.getLineHeight());
        TextureRegion[] walkFrames = new TextureRegion[8];
        for (int i = 0; i < 8; i++) {
            Texture texture = new Texture("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\textures\\frames\\frame" + (i + 1) + ".png");
            walkFrames[i] = new TextureRegion(texture);
        }
        walkAnimation = new Animation<>(0.1f, walkFrames);
        idleTexture = new Texture("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\textures\\frames\\frame1.png");

        map = new TmxMapLoader().load("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\maps\\escaperoom.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        int mapWidth = map.getProperties().get("width", Integer.class);
        int mapHeight = map.getProperties().get("height", Integer.class);
        float scaledPlayerWidth = walkFrames[0].getRegionWidth() * PLAYER_SCALE;
        float scaledPlayerHeight = walkFrames[0].getRegionHeight() * PLAYER_SCALE;
        int windowWidth = mapWidth * TILE_WIDTH;
        int windowHeight = mapHeight * TILE_HEIGHT;
        Gdx.graphics.setWindowedMode(windowWidth, windowHeight);

     // Crear la textura en blanco
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        blankTexture = new Texture(pixmap);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, windowWidth / ZOOM_SCALE, windowHeight / ZOOM_SCALE);
        camera.update();

        // Crear la fuente para el diálogo
        dialogFont = new BitmapFont();
        dialogFont.setColor(Color.WHITE);
        dialogFont.getData().setScale(fontSize2 / font.getLineHeight());
        
        playerPosition = new Vector2(windowWidth / 2, windowHeight / 2);
        previousPlayerPosition = new Vector2(playerPosition);
        playerRect = new Rectangle(playerPosition.x - scaledPlayerWidth / 2, playerPosition.y - scaledPlayerHeight / 2, scaledPlayerWidth, scaledPlayerHeight);
        
        // Calcular la escala del mapa
        MapProperties mapProperties = map.getProperties();
        int mapTileWidth = mapProperties.get("tilewidth", Integer.class);
        int mapTileHeight = mapProperties.get("tileheight", Integer.class);
        int mapPixelWidth = mapProperties.get("width", Integer.class) * mapTileWidth;
        int mapPixelHeight = mapProperties.get("height", Integer.class) * mapTileHeight;
        mapScale = (float) windowWidth / mapPixelWidth;
       
        spawnLucius(40,270);
        spawnBiblio(600, 280);
        spawnDorm(620, 830);
        spawnCocina(190, 560);
        spawnFinal(360, 865);
        createPlayer(); // Llamado al método createPlayer() para configurar la posición inicial del jugador
    }
    private boolean isNearDoor() {
        MapLayer doorLayer = map.getLayers().get("puertas");

        if (doorLayer != null) {
            for (MapObject object : doorLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    // Ajusta la posición de la puerta según la escala del mapa
                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    // Calcula el centro de la puerta
                    float doorCenterX = doorX + doorWidth / 2;
                    float doorCenterY = doorY + doorHeight / 2;

                    // Calcula la distancia entre el personaje y el centro de la puerta
                    float distance = playerPosition.dst(doorCenterX, doorCenterY);

                    // Define la distancia de proximidad para la interacción con la puerta
                    float proximityDistance = 8; // Ajusta este valor según tus necesidades

                    if (distance <= proximityDistance) {
                        return true; // El personaje está cerca de una puerta
                    }
                }
            }
        }

        return false; // El personaje no está cerca de ninguna puerta
    }
    private boolean isNearLoot() {
        MapLayer doorLayer = map.getLayers().get("Loot");

        if (doorLayer != null) {
            for (MapObject object : doorLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    // Ajusta la posición de la puerta según la escala del mapa
                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    // Calcula el centro de la puerta
                    float doorCenterX = doorX + doorWidth / 2;
                    float doorCenterY = doorY + doorHeight / 2;

                    // Calcula la distancia entre el personaje y el centro de la puerta
                    float distance = playerPosition.dst(doorCenterX, doorCenterY);

                    // Define la distancia de proximidad para la interacción con la puerta
                    float proximityDistance = 8; // Ajusta este valor según tus necesidades

                    if (distance <= proximityDistance) {
                        return true; // El personaje está cerca de una puerta
                    }
                }
            }
        }

        return false; // El personaje no está cerca de ninguna puerta
    }
    private boolean isNearExpendora() {
        MapLayer doorLayer = map.getLayers().get("Monster");

        if (doorLayer != null) {
            for (MapObject object : doorLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    // Ajusta la posición de la puerta según la escala del mapa
                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    // Calcula el centro de la puerta
                    float doorCenterX = doorX + doorWidth / 2;
                    float doorCenterY = doorY + doorHeight / 2;

                    // Calcula la distancia entre el personaje y el centro de la puerta
                    float distance = playerPosition.dst(doorCenterX, doorCenterY);

                    // Define la distancia de proximidad para la interacción con la puerta
                    float proximityDistance = 8; // Ajusta este valor según tus necesidades

                    if (distance <= proximityDistance) {
                        return true; // El personaje está cerca de una puerta
                    }
                }
            }
        }

        return false; // El personaje no está cerca de ninguna puerta
    }
    
    private boolean isNearBotella() {
        MapLayer doorLayer = map.getLayers().get("Alcohol");

        if (doorLayer != null) {
            for (MapObject object : doorLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    // Ajusta la posición de la puerta según la escala del mapa
                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    // Calcula el centro de la puerta
                    float doorCenterX = doorX + doorWidth / 2;
                    float doorCenterY = doorY + doorHeight / 2;

                    // Calcula la distancia entre el personaje y el centro de la puerta
                    float distance = playerPosition.dst(doorCenterX, doorCenterY);

                    // Define la distancia de proximidad para la interacción con la puerta
                    float proximityDistance = 8; // Ajusta este valor según tus necesidades

                    if (distance <= proximityDistance) {
                        return true; // El personaje está cerca de una puerta
                    }
                }
            }
        }

        return false; // El personaje no está cerca de ninguna puerta
    }
    private boolean isNearTorta() {
        MapLayer doorLayer = map.getLayers().get("Torta");

        if (doorLayer != null) {
            for (MapObject object : doorLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    // Ajusta la posición de la puerta según la escala del mapa
                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    // Calcula el centro de la puerta
                    float doorCenterX = doorX + doorWidth / 2;
                    float doorCenterY = doorY + doorHeight / 2;

                    // Calcula la distancia entre el personaje y el centro de la puerta
                    float distance = playerPosition.dst(doorCenterX, doorCenterY);

                    // Define la distancia de proximidad para la interacción con la puerta
                    float proximityDistance = 8; // Ajusta este valor según tus necesidades

                    if (distance <= proximityDistance) {
                        return true; // El personaje está cerca de una puerta
                    }
                }
            }
        }

        return false; // El personaje no está cerca de ninguna puerta
    }
    private boolean isNearPoster() {
        MapLayer posterLayer = map.getLayers().get("Poster");

        if (posterLayer != null) {
            for (MapObject object : posterLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    // Ajusta la posición de la puerta según la escala del mapa
                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    // Calcula el centro de la puerta
                    float doorCenterX = doorX + doorWidth / 2;
                    float doorCenterY = doorY + doorHeight / 2;

                    // Calcula la distancia entre el personaje y el centro de la puerta
                    float distance = playerPosition.dst(doorCenterX, doorCenterY);

                    // Define la distancia de proximidad para la interacción con la puerta
                    float proximityDistance = 8; // Ajusta este valor según tus necesidades

                    if (distance <= proximityDistance) {
                        return true; // El personaje está cerca de una puerta
                    }
                }
            }
        }

        return false; // El personaje no está cerca de ninguna puerta
    }
    private boolean isNearMenu() {
        MapLayer posterLayer = map.getLayers().get("cajonsala");

        if (posterLayer != null) {
            for (MapObject object : posterLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    // Ajusta la posición de la puerta según la escala del mapa
                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    // Calcula el centro de la puerta
                    float doorCenterX = doorX + doorWidth / 2;
                    float doorCenterY = doorY + doorHeight / 2;

                    // Calcula la distancia entre el personaje y el centro de la puerta
                    float distance = playerPosition.dst(doorCenterX, doorCenterY);

                    // Define la distancia de proximidad para la interacción con la puerta
                    float proximityDistance = 8; // Ajusta este valor según tus necesidades

                    if (distance <= proximityDistance) {
                        return true; // El personaje está cerca de una puerta
                    }
                }
            }
        }

        return false; // El personaje no está cerca de ninguna puerta
    }
    
    private boolean isNearEscritorio() {
        MapLayer posterLayer = map.getLayers().get("Escritorio");

        if (posterLayer != null) {
            for (MapObject object : posterLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    // Ajusta la posición de la puerta según la escala del mapa
                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    // Calcula el centro de la puerta
                    float doorCenterX = doorX + doorWidth / 2;
                    float doorCenterY = doorY + doorHeight / 2;

                    // Calcula la distancia entre el personaje y el centro de la puerta
                    float distance = playerPosition.dst(doorCenterX, doorCenterY);

                    // Define la distancia de proximidad para la interacción con la puerta
                    float proximityDistance = 8; // Ajusta este valor según tus necesidades

                    if (distance <= proximityDistance) {
                        return true; // El personaje está cerca de una puerta
                    }
                }
            }
        }

        return false; // El personaje no está cerca de ninguna puerta
    }
    private boolean isNearLucius() {
        MapLayer monsterLayer = map.getLayers().get("Monstruo");

        if (monsterLayer != null) {
            for (MapObject object : monsterLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    // Ajusta la posición de la puerta según la escala del mapa
                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    // Calcula el centro de la puerta
                    float doorCenterX = doorX + doorWidth / 2;
                    float doorCenterY = doorY + doorHeight / 2;

                    // Calcula la distancia entre el personaje y el centro de la puerta
                    float distance = playerPosition.dst(doorCenterX, doorCenterY);

                    // Define la distancia de proximidad para la interacción con la puerta
                    float proximityDistance = 8; // Ajusta este valor según tus necesidades

                    if (distance <= proximityDistance) {
                        return true; // El personaje está cerca de una puerta
                    }
                }
            }
        }

        return false; // El personaje no está cerca de ninguna puerta
    }
    
    private boolean isNearChef() {
        MapLayer monsterLayer = map.getLayers().get("Chef");

        if (monsterLayer != null) {
            for (MapObject object : monsterLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    // Ajusta la posición de la puerta según la escala del mapa
                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    // Calcula el centro de la puerta
                    float doorCenterX = doorX + doorWidth / 2;
                    float doorCenterY = doorY + doorHeight / 2;

                    // Calcula la distancia entre el personaje y el centro de la puerta
                    float distance = playerPosition.dst(doorCenterX, doorCenterY);

                    // Define la distancia de proximidad para la interacción con la puerta
                    float proximityDistance = 8; // Ajusta este valor según tus necesidades

                    if (distance <= proximityDistance) {
                        return true; // El personaje está cerca de una puerta
                    }
                }
            }
        }
        return false; // El personaje no está cerca de ninguna puerta
    }
    
    private boolean isNearMorgana() {
        MapLayer monsterLayer = map.getLayers().get("Morgana");

        if (monsterLayer != null) {
            for (MapObject object : monsterLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    // Ajusta la posición de la puerta según la escala del mapa
                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    // Calcula el centro de la puerta
                    float doorCenterX = doorX + doorWidth / 2;
                    float doorCenterY = doorY + doorHeight / 2;

                    // Calcula la distancia entre el personaje y el centro de la puerta
                    float distance = playerPosition.dst(doorCenterX, doorCenterY);

                    // Define la distancia de proximidad para la interacción con la puerta
                    float proximityDistance = 8; // Ajusta este valor según tus necesidades

                    if (distance <= proximityDistance) {
                        return true; // El personaje está cerca de una puerta
                    }
                }
            }
        }
        return false; // El personaje no está cerca de ninguna puerta
    }
    
    private boolean isNearSennor() {
        MapLayer monsterLayer = map.getLayers().get("Sennor");

        if (monsterLayer != null) {
            for (MapObject object : monsterLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    // Ajusta la posición de la puerta según la escala del mapa
                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    // Calcula el centro de la puerta
                    float doorCenterX = doorX + doorWidth / 2;
                    float doorCenterY = doorY + doorHeight / 2;

                    // Calcula la distancia entre el personaje y el centro de la puerta
                    float distance = playerPosition.dst(doorCenterX, doorCenterY);

                    // Define la distancia de proximidad para la interacción con la puerta
                    float proximityDistance = 8; // Ajusta este valor según tus necesidades

                    if (distance <= proximityDistance) {
                        return true; // El personaje está cerca de una puerta
                    }
                }
            }
        }
        return false; // El personaje no está cerca de ninguna puerta
    }
    private boolean isNearErebos() {
        MapLayer monsterLayer = map.getLayers().get("Erebos");

        if (monsterLayer != null) {
            for (MapObject object : monsterLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    // Ajusta la posición de la puerta según la escala del mapa
                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    // Calcula el centro de la puerta
                    float doorCenterX = doorX + doorWidth / 2;
                    float doorCenterY = doorY + doorHeight / 2;

                    // Calcula la distancia entre el personaje y el centro de la puerta
                    float distance = playerPosition.dst(doorCenterX, doorCenterY);

                    // Define la distancia de proximidad para la interacción con la puerta
                    float proximityDistance = 8; // Ajusta este valor según tus necesidades

                    if (distance <= proximityDistance) {
                        return true; // El personaje está cerca de una puerta
                    }
                }
            }
        }
        return false; // El personaje no está cerca de ninguna puerta
    }
    
   
    @Override
    public void render() {
        handleInput();
        updateCamera();
        updateAnimation();
        
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if(Luciusvivo) {
        	renderMonster(batch);
        }
        if(Chefvivo) {
        	renderMonster4(batch);
        }      
        renderMonster2(batch);
        renderMonster3(batch);
        
        renderMonster5(batch);
        TextureRegion currentFrame = getCurrentFrame();
        batch.draw(
            currentFrame,
            playerPosition.x - currentFrame.getRegionWidth() * PLAYER_SCALE / 2,
            playerPosition.y - currentFrame.getRegionHeight() * PLAYER_SCALE / 2,
            currentFrame.getRegionWidth() * PLAYER_SCALE,
            currentFrame.getRegionHeight() * PLAYER_SCALE
        );

        if (isNearDoor()) {
            font.draw(batch, "Presione E para abrir", playerPosition.x, playerPosition.y + 20);
        }
        if (isNearLoot()||isNearExpendora()) {
            font.draw(batch, "Presione F para abrir", playerPosition.x, playerPosition.y + 20);
        }
        if (isNearBotella()) {
            font.draw(batch, "Presione F para tomar alcohol", playerPosition.x, playerPosition.y + 20);
        }
        if (isNearTorta()) {
            font.draw(batch, "Presione F para"+"\n+"+"tomar pedazo de torta", playerPosition.x, playerPosition.y + 20);
        }
        if (isNearLucius() || isNearChef() || isNearMorgana() || isNearSennor()||isNearErebos()) {
            font.draw(batch, "Presione D para hablar ", playerPosition.x, playerPosition.y + 20);
        }
        if(isNearMorgana()&&habla1Bruja) {
        	font.draw(batch,"Presione D para hablar "+"\n"+"Presione E para entregar objetos ", playerPosition.x, playerPosition.y + 20);
        }
        if (isNearPoster() && isNearEscritorio()) {
            font.draw(batch, "Presione F para abrir"+"\n"+"Presione E para leer", playerPosition.x, playerPosition.y + 20);
        }
        if (isNearMenu()) {
            font.draw(batch,"Presione E para leer", playerPosition.x, playerPosition.y + 20);
        }
        if (isNearLucius()&&Gdx.input.isKeyJustPressed(Input.Keys.D)) {
        	separatedDialogs = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\Lucius.txt"));
        } 
        if (isNearErebos()&&Gdx.input.isKeyJustPressed(Input.Keys.D)) {
        	if(hasPocion) {
            	separatedDialogs = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\Batalla.pocion.txt"));

        	}else {
            	separatedDialogs = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\Batalla.txt"));

        	}
        } 
        if (isNearLucius()&& resplucius&&habla1Lucius) {
        	separatedDialogs = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\respcorrectaLucius.txt"));
        } 
        if (isNearLucius()&& !resplucius&&habla1Lucius) {
        	separatedDialogs = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\respincorrectaLucius.txt"));
        } 
        if (isNearChef()&& respchef&&habla1Chef) {
        	separatedDialogs = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\respcorrectaChef.txt"));
        } 
        if (isNearChef()&& !respchef&&habla1Chef) {
        	separatedDialogs = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\respincorrectaChef.txt"));
        } 
        if (isNearChef()&&Gdx.input.isKeyJustPressed(Input.Keys.D)) {
        	separatedDialogs = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\Gustavo.txt"));
        }
        if (isNearMorgana()&&Gdx.input.isKeyJustPressed(Input.Keys.D)) {
        	separatedDialogs = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\Morgana.txt"));
        }
        if (isNearMorgana()&&Gdx.input.isKeyJustPressed(Input.Keys.E)) {
        	if(verificarobjetos()) {
        		separatedDialogs = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\ObjetosObt.txt"));
        		
        	}else {
        		separatedDialogs = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\ObjetosnoObt.txt"));      		
        	}
        	
        }
        if (isNearSennor()&&Gdx.input.isKeyJustPressed(Input.Keys.D)) {
        	separatedDialogs = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\Señor.txt"));
        }  
        if (isNearSennor()&&Gdx.input.isKeyJustPressed(Input.Keys.D)&&habla1Sennor) {
        	separatedDialogs = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\Sennor2.txt"));
        } 
        if (isShowingDialog) {
        	
            batch.setProjectionMatrix(camera.combined);
            batch2.begin();
            
            batch2.setColor(0, 0, 0, 0.8f); // Establecer el color a negro con una transparencia del 80%
            batch2.draw(blankTexture, 0, 0, Gdx.graphics.getWidth(), 100); // Dibujar un rectángulo negro en la parte superior de la pantalla

            // Dibujar el texto del diálogo
            
            dialogFont.setColor(Color.WHITE); // Establecer el color del texto a blanco
            
           
            monsterDialogLines = separatedDialogs.get(currentDialogLine); 
            
            String currentLine ="";
            for(int i=0; i<monsterDialogLines.size(); i++ ) {
            	currentLine+=monsterDialogLines.get(i)+"\n";
            }
            /*String currentLine = monsterDialogLines.toString();  */
            
            
            dialogFont.draw(batch2, currentLine, 10, 95);
          
            // Restaurar el color predeterminado           
            batch2.end();
        }
        if (showinput&&isNearLucius()) {
        	Inventario inv= new Inventario();
        	inputText = JOptionPane.showInputDialog(null, "Ingrese el nombre de la cancion:");

            // Haz algo con el texto ingresado, como guardarlo en una variable
        	String nom=inputText;
        	respdada=true;
            System.out.println("Texto ingresado: " + nom);
            if(verificaresLucius(nom)) {
            	resplucius=true;
            	isShowingDialog=true;
            	String attributes= "1,llavedecocina,2,1000,5,puertaCocina,false,222,222";
				inv.addItem(attributes);
				mostrarMensajeEmergente("Recibiste Llave Cocina");											
            }else {
            	resplucius=false;
            	isShowingDialog=true;
            }
            showinput=false;
            
        }
        if (showinput&&isNearChef()) {
        	Inventario inv= new Inventario();
        	inputText = JOptionPane.showInputDialog(null, "Ingrese el ingrediente faltante:");

            // Haz algo con el texto ingresado, como guardarlo en una variable
        	String nom=inputText;
        	respdadachef=true;
            System.out.println("Texto ingresado: " + nom);
            if(verificaresChef(nom)) {
            	respchef=true;
            	isShowingDialog=true;
            	String attributes= "1,llavedecuarto,2,1000,5,puertaCuarto,false,333,333";
				inv.addItem(attributes);
				mostrarMensajeEmergente("Recibiste Llave Cuarto");											
            }else {
            	respchef=false;
            	isShowingDialog=true;
            }
            showinput=false;
            
        }
        batch.end();
        
    }
    public void darpocion() {
    	Inventario inv= new Inventario();
    	String attributes= "3,pocionsuerte,2,1000,5,pocionSuerte,false,666,666";
		inv.addItem(attributes);
		mostrarMensajeEmergente("Recibiste Pocion de Suerte");	
		hasPocion=true;
    }
    
    public void dispose() {
        batch.dispose();
        batch2.dispose();
        idleTexture.dispose();
        map.dispose();
        mapRenderer.dispose();
        font.dispose();
        blankTexture.dispose();
    }

    private void handleInput() {
        float speed = 2.0f;
        Inventario inv= new Inventario();
        // Guardar la posición actual del jugador
        previousPlayerPosition.set(playerPosition);
       
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerPosition.x -= speed;
            playerRect.x = playerPosition.x - getCurrentFrame().getRegionWidth() * PLAYER_SCALE / 2;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerPosition.x += speed;
            playerRect.x = playerPosition.x - getCurrentFrame().getRegionWidth() * PLAYER_SCALE / 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerPosition.y += speed;
            playerRect.y = playerPosition.y - getCurrentFrame().getRegionHeight() * PLAYER_SCALE / 2;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerPosition.y -= speed;
            playerRect.y = playerPosition.y - getCurrentFrame().getRegionHeight() * PLAYER_SCALE / 2;
        }
        if (isNearMorgana()&&Gdx.input.isKeyJustPressed(Input.Keys.E)) {
        	if(verificarobjetos()) {
        		currentDialogLine = 0;  
        		separatedDialogs2 = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\ObjetosObt.txt"));
        		darpocion();
        		isShowingDialog=true;
        	}else {
        		 currentDialogLine = 0;  
        		separatedDialogs2 = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\ObjetosnoObt.txt"));
        		isShowingDialog=true;
        	}
        	
        }
        // Verificar la colisión después de actualizar la posición del jugador
        if (detectCollision()) {
            // Se encontró una colisión, restablecer la posición del jugador
            playerPosition.set(previousPlayerPosition);
            playerRect.x = previousPlayerPosition.x - getCurrentFrame().getRegionWidth() * PLAYER_SCALE / 2;
            playerRect.y = previousPlayerPosition.y - getCurrentFrame().getRegionHeight() * PLAYER_SCALE / 2;
        }    
        if (isNearDoor() && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            String doorName = whichDoor();

            if (doorName != null) {
                if (doorName.equals("puertaBar")) {
                    if (abrirpuerta()) {
                        changeRoom();
                    } else {
                        System.out.println("nopapi");
                        mostrarMensajeEmergente("Puerta Cerrada");
                    }
                } else if (doorName.equals("puertaCuarto")) {
                    System.out.println("AAAAAAA");
                    if (abrirpuerta()) {
                        teleport(585, 785);
                    } else {
                        System.out.println("nopapi");
                        mostrarMensajeEmergente("Puerta Cerrada");
                    }
                } else if (doorName.equals("puertaCocina")) {
                    if (abrirpuerta()) {
                        changeRoom();
                    } else {
                        System.out.println("nopapi");
                        mostrarMensajeEmergente("Puerta Cerrada");
                    }
                } else if (doorName.equals("puertaPasillo")) {
                    teleport(390, 530);
                } else if (doorName.equals("puertaPasillo2")) {
                    teleport(325, 400);
                }
                else if (doorName.equals("puertaBar2")) {
                    teleport(235, 348);
                }else if (doorName.equals("puertaCuarto2")) {
                    teleport(600, 660);
                }else if (doorName.equals("puertaBiblioteca2")) {
                    teleport(360, 340
                    		);
                }else if (doorName.equals("puertaCocina2")) {
                    teleport(275, 400);
                } else if (doorName.equals("puertaBiblioteca")) {
                    if (abrirpuerta()) {
                        changeRoom();
                    } else {
                        System.out.println("nopapi");
                        mostrarMensajeEmergente("Puerta Cerrada");
                    }
                } else if (doorName.equals("puertaCalabozo")) {
                    if (abrirpuerta()) {
                        changeRoom();
                    } else {
                        System.out.println("nopapi");
                        mostrarMensajeEmergente("Puerta Cerrada");
                    }
                }
            }
        }
  	
        if (isNearLoot() && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            abrircajon();
        } 
        if (isNearExpendora() && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            abrirMaquinaExpendedora();
        }
        if (isNearBotella() && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
        	tomaralcohol();
        }
        if (isNearTorta() && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
        	tomarTorta();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
        	mostrarVentanaEmergentePelar();
        } 
        if (isNearErebos()&&Gdx.input.isKeyJustPressed(Input.Keys.D)) {
        	if(hasPocion) {
            	separatedDialogs2 = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\Batalla.pocion.txt"));
            	currentDialogLine = 0;   
                
            	isShowingDialog = true;
        	}else {
            	separatedDialogs2 = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\Batalla.txt"));
            	currentDialogLine = 0;   
                
            	isShowingDialog = true;
        	}
        }
        if (isNearLucius()&&Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            // Cargar los diálogos del archivo   
        	separatedDialogs2 = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\Lucius.txt"));
            currentDialogLine = 0;   
            
        	isShowingDialog = true;
        } 
        if (isNearLucius()&& resplucius&&habla1Lucius&&isShowingDialog) {
            // Cargar los diálogos del archivo   
        	separatedDialogs2 = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\respcorrectaLucius.txt"));
            currentDialogLine = 0;   
        } 
        if (isNearLucius()&& !resplucius&&habla1Lucius&&isShowingDialog) {
            // Cargar los diálogos del archivo   
        	separatedDialogs2 = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\respincorrectaLucius.txt"));
            currentDialogLine = 0;   
        } 
        if (isNearChef()&&Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            // Cargar los diálogos del archivo   
        	separatedDialogs2 = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\Gustavo.txt"));
            currentDialogLine = 0;                       
        	isShowingDialog = true;
        } 
        if (isNearChef()&& respchef&&habla1Chef&&isShowingDialog) {
            // Cargar los diálogos del archivo   
        	separatedDialogs2 = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\respcorrectaChef.txt"));
            currentDialogLine = 0;   
        } 
        if (isNearChef()&& !respchef&&habla1Chef&&isShowingDialog) {
            // Cargar los diálogos del archivo   
        	separatedDialogs2 = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\respincorrectaChef.txt"));
            currentDialogLine = 0;   
        } 
        if (isNearMorgana()&&Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            // Cargar los diálogos del archivo   
        	separatedDialogs2 = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\Morgana.txt"));
            currentDialogLine = 0;                       
        	isShowingDialog = true;
        } 
        if (isNearSennor()&&Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            // Cargar los diálogos del archivo   
        	separatedDialogs2 = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\Señor.txt"));
            currentDialogLine = 0;                       
        	isShowingDialog = true;
        } 
        if (isNearSennor()&&Gdx.input.isKeyJustPressed(Input.Keys.D)&&habla1Sennor) {
            // Cargar los diálogos del archivo   
        	separatedDialogs2 = splitDialogLines(loadMonsterDialogFromFile("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\dialogos\\Sennor2.txt"));
            currentDialogLine = 0;                       
        	isShowingDialog = true;
        } 
        if(isNearPoster()&&Gdx.input.isKeyJustPressed(Input.Keys.E)) {
        	mostrarImagenEmergente("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\Poster\\Lucius.png", "Poster",500,700);
        }
        if(isNearMenu()&&Gdx.input.isKeyJustPressed(Input.Keys.E)) {
        	mostrarImagenEmergente("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\Poster\\restaurantmenu.png", "Menu",500,700);
        }
        if(isShowingDialog&&Gdx.input.isKeyJustPressed(Input.Keys.ENTER)&&(currentDialogLine<=separatedDialogs2.size())){
        	System.out.println(currentDialogLine);
        		currentDialogLine++;
        }
        if(isShowingDialog&&Gdx.input.isKeyJustPressed(Input.Keys.ENTER)&&(currentDialogLine==separatedDialogs2.size())){
        	isShowingDialog = false;        
        	if(!isShowingDialog&&isNearLucius()&&habla1Lucius) {
        		habla2Lucius = true;
        		if(!resplucius) {
        			mostrarVentanaEmergentePelar();
        		}
        	}
        	if(!isShowingDialog&&isNearChef()&&habla1Chef) {
        		habla2Chef = true;
        		if(!respchef) {
        			mostrarVentanaEmergentePelar();
        		}
        	}
        	if(!isShowingDialog&&isNearLucius()&&!habla1Lucius) {
        		habla1Lucius=true;
        		System.out.println("AAAAAAA");
        	}
        	if(!isShowingDialog&&isNearErebos()&&!habla1Erebos) {
        		habla1Erebos=true;
        		if(hasPocion) {
        			mostrarVentanaEmergentePelar2();
        		}else {
        			mostrarVentanaEmergentePelar3();
        		}
        		
        	}
        	if(!isShowingDialog&&isNearChef()&&!habla1Chef) {
        		habla1Chef=true;
        		System.out.println("AAAAAAA");
        	}
        	if(!isShowingDialog&&isNearMorgana()&&!habla1Bruja) {
        		habla1Bruja=true;
        		String attributes= "1,llavedecalabozo,2,1000,5,puertaCalabozo,false,555,555";
				inv.addItem(attributes);
				mostrarMensajeEmergente("Recibiste Llave del Calabozo");
        		
        	}
        	if(!isShowingDialog&&isNearSennor()&&!habla1Sennor) {
        		habla1Sennor=true;
        		String attributes= "1,llavedebiblioteca,2,1000,5,puertaBiblioteca,false,444,444";
				inv.addItem(attributes);
				mostrarMensajeEmergente("Recibiste Llave Biblioteca");
        	}
        }
        if(!isShowingDialog&&isNearLucius()&&habla1Lucius&&!respdada) {
        	System.out.println("IOII");
        	showinput=true;
        }
        if(!isShowingDialog&&isNearChef()&&habla1Chef&&!respdadachef) {
        	System.out.println("EEEE");
        	showinput=true;
        }
 
    }

    private boolean verificaresLucius(String nom) {
    	String respa= "Albinoni-Adagio";
    	String respb= "Adagio";
    	String respc = "adagio";
    	String respd= "albinoni-adagio";
    	if (nom.equals(respa)||nom.equals(respb)||nom.equals(respc)||nom.equals(respd)){
    		return true;
    	}else {
    		return false;
    	}
    }
    private boolean verificaresChef(String nom) {
    	String respa= "Pescado";
    	String respb= "PESCADO";
    	String respc = "pescado";
    	if (nom.equals(respa)||nom.equals(respb)||nom.equals(respc)){
    		return true;
    	}else {
    		return false;
    	}
    }
    public static void mostrarMensajeEmergente(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
    }
    
    private List<List<String>> splitDialogLines(List<String> dialogLines) {
        List<List<String>> separatedDialogs = new ArrayList<>();
        List<String> currentDialog = new ArrayList<>();

        for (String line : dialogLines) {
            if (line.equals("**")) {
                if (!currentDialog.isEmpty()) {
                    separatedDialogs.add(currentDialog);
                    currentDialog = new ArrayList<>();
                }
            } else {
                currentDialog.add(line);
            }
        }

        if (!currentDialog.isEmpty()) {
            separatedDialogs.add(currentDialog);
        }

        return separatedDialogs;
        
    }
    
    private List<String> loadMonsterDialogFromFile(String filePath) {
        List<String> dialogLines = new ArrayList<>();

        try {
            InputStream inputStream = Gdx.files.internal(filePath).read();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                dialogLines.add(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dialogLines;
    }
    
    public void mostrarVentanaEmergentePelar() {
        final JDialog dialogo = new JDialog();
        dialogo.setTitle("Ventana Emergente Pelar");
        dialogo.setSize(300, 200);
        dialogo.setLocationRelativeTo(null);
        dialogo.setLayout(new BorderLayout());
        dialogo.setDefaultCloseOperation(dialogo.DO_NOTHING_ON_CLOSE);
       
        // Crea un JLabel para el fondo de la ventana emergente
        ImageIcon imagenFondo = new ImageIcon("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\Fondos\\dados2.jpg");
        JLabel fondo = new JLabel(imagenFondo);
        fondo.setBounds(0, 0, 300, 200);
        fondo.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setOpaque(false); // Hace que el panel sea transparente para que se vea el fondo
        panel.setLayout(new GridLayout(3, 2));

        JLabel etiquetaTiradaUsuario = new JLabel("    Tu tirada:");
        etiquetaTiradaUsuario.setForeground(java.awt.Color.white);
        final JTextField campoTiradaUsuario = new JTextField(5);

        JLabel etiquetaTiradaMonstruo = new JLabel("    Tirada del Monstruo:");
        etiquetaTiradaMonstruo.setForeground(java.awt.Color.white);
        final JTextField campoTiradaMonstruo = new JTextField(5);

        JLabel etiquetaResultado = new JLabel("    Resultado");
        etiquetaResultado.setForeground(java.awt.Color.white);
        final JLabel resultado = new JLabel();
        resultado.setForeground(java.awt.Color.white);
        final JButton botonPelar = new JButton("Pelear");
        final JButton botonFinalizarTurno = new JButton("Finalizar Turno");
        botonFinalizarTurno.setEnabled(false);
        
        botonPelar.addActionListener(new ActionListener() {
        	 
            @Override
            public void actionPerformed(ActionEvent e) {
                Random random = new Random();
                int tiradaUsuario = random.nextInt(6) + 1;
                int tiradaMonstruo = random.nextInt(6) + 1;

                campoTiradaUsuario.setText(String.valueOf("                 "+tiradaUsuario));
                campoTiradaMonstruo.setText(String.valueOf("                 "+tiradaMonstruo));

                int resultadoTirada = Integer.compare(tiradaUsuario, tiradaMonstruo);
                if (resultadoTirada > 0) {
                    resultado.setText("      ¡Ganaste!");
                    botonPelar.setEnabled(false);
                    botonFinalizarTurno.setEnabled(true);
 
                } else if (resultadoTirada < 0) {
                	resultado.setText("      ¡Perdiste!");
                	botonPelar.setEnabled(false);
                	 botonFinalizarTurno.setEnabled(true);
                } else {
                    resultado.setText("      ¡Empate!");
                    
                }
               
                
            }
        });
        botonFinalizarTurno.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Inventario inv = new Inventario();
				// TODO Auto-generated method stub
				if(resultado.getText().equals("      ¡Perdiste!")) {
					dialogo.dispose();
					perdiste();					
				}else {
					dialogo.dispose();
					System.out.println("wii");
					if(isNearLucius()) {
						Luciusvivo=false;
						String attributes= "1,llavedecocina,2,1000,5,puertaCocina,false,111,111";
						inv.addItem(attributes);
						mostrarMensajeEmergente("Recibiste Llave Cocina");
					}
					if(isNearChef()) {
						Chefvivo=false;
						String attributes= "1,llavedecuarto,2,1000,5,puertaCuarto,false,333,333";
						inv.addItem(attributes);
						mostrarMensajeEmergente("Recibiste Llave Cuarto");
					}
					
				}
			}
        	
        });
        
        panel.add(etiquetaTiradaUsuario);
        panel.add(campoTiradaUsuario);
        panel.add(etiquetaTiradaMonstruo);
        panel.add(campoTiradaMonstruo);
        panel.add(etiquetaResultado);
        panel.add(resultado);

        fondo.add(panel, BorderLayout.CENTER);
       
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new GridLayout(1, 2));
        panelBotones.add(botonPelar);
        panelBotones.add(botonFinalizarTurno);
        fondo.add(panelBotones, BorderLayout.SOUTH);
        dialogo.setContentPane(fondo);
        dialogo.setVisible(true);
        
       
    }
    public void mostrarVentanaEmergentePelar3() {
        final JDialog dialogo = new JDialog();
        dialogo.setTitle("Ventana Emergente Pelea Final");
        dialogo.setSize(300, 200);
        dialogo.setLocationRelativeTo(null);
        dialogo.setLayout(new BorderLayout());
        dialogo.setDefaultCloseOperation(dialogo.DO_NOTHING_ON_CLOSE);
       
        // Crea un JLabel para el fondo de la ventana emergente
        ImageIcon imagenFondo = new ImageIcon("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\Fondos\\dados2.jpg");
        JLabel fondo = new JLabel(imagenFondo);
        fondo.setBounds(0, 0, 300, 200);
        fondo.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setOpaque(false); // Hace que el panel sea transparente para que se vea el fondo
        panel.setLayout(new GridLayout(3, 2));

        JLabel etiquetaTiradaUsuario = new JLabel("    Tu tirada:");
        etiquetaTiradaUsuario.setForeground(java.awt.Color.white);
        final JTextField campoTiradaUsuario = new JTextField(5);

        JLabel etiquetaTiradaMonstruo = new JLabel("    Tirada del Monstruo:");
        etiquetaTiradaMonstruo.setForeground(java.awt.Color.white);
        final JTextField campoTiradaMonstruo = new JTextField(5);

        JLabel etiquetaResultado = new JLabel("    Resultado");
        etiquetaResultado.setForeground(java.awt.Color.white);
        final JLabel resultado = new JLabel();
        resultado.setForeground(java.awt.Color.white);
        final JButton botonPelar = new JButton("Pelear");
        final JButton botonFinalizarTurno = new JButton("Finalizar Turno");
        botonFinalizarTurno.setEnabled(false);
        
        botonPelar.addActionListener(new ActionListener() {
        	 
            @Override
            public void actionPerformed(ActionEvent e) {
                Random random = new Random();
                int tiradaUsuario = random.nextInt(12) + 1;
                int tiradaMonstruo = random.nextInt(12) + 1;

                campoTiradaUsuario.setText(String.valueOf("                 "+tiradaUsuario));
                campoTiradaMonstruo.setText(String.valueOf("                 "+tiradaMonstruo));

                int resultadoTirada = Integer.compare(tiradaUsuario, tiradaMonstruo);
                if (resultadoTirada > 0) {
                    resultado.setText("      ¡Ganaste!");
                    botonPelar.setEnabled(false);
                    botonFinalizarTurno.setEnabled(true);
 
                } else if (resultadoTirada < 0) {
                	resultado.setText("      ¡Perdiste!");
                	botonPelar.setEnabled(false);
                	 botonFinalizarTurno.setEnabled(true);
                } else {
                    resultado.setText("      ¡Empate!");
                    
                }
               
                
            }
        });
        botonFinalizarTurno.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Inventario inv = new Inventario();
				// TODO Auto-generated method stub
				if(resultado.getText().equals("      ¡Perdiste!")) {
					dialogo.dispose();
					perdiste();	
					Gdx.app.exit();
				}else {
					dialogo.dispose();
					System.out.println("wii");					
					mostrarImagenEmergente("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\Poster\\win.png", "GANASTE", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
					Gdx.app.exit();
				}
			}
        	
        });
        
        panel.add(etiquetaTiradaUsuario);
        panel.add(campoTiradaUsuario);
        panel.add(etiquetaTiradaMonstruo);
        panel.add(campoTiradaMonstruo);
        panel.add(etiquetaResultado);
        panel.add(resultado);

        fondo.add(panel, BorderLayout.CENTER);
       
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new GridLayout(1, 2));
        panelBotones.add(botonPelar);
        panelBotones.add(botonFinalizarTurno);
        fondo.add(panelBotones, BorderLayout.SOUTH);
        dialogo.setContentPane(fondo);
        dialogo.setVisible(true);
        
       
    }
    public void mostrarVentanaEmergentePelar2() {
        final JDialog dialogo = new JDialog();
        dialogo.setTitle("Ventana Emergente Pelea Final");
        dialogo.setSize(300, 200);
        dialogo.setLocationRelativeTo(null);
        dialogo.setLayout(new BorderLayout());
        dialogo.setDefaultCloseOperation(dialogo.DO_NOTHING_ON_CLOSE);
       
        // Crea un JLabel para el fondo de la ventana emergente
        ImageIcon imagenFondo = new ImageIcon("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\Fondos\\dados2.jpg");
        JLabel fondo = new JLabel(imagenFondo);
        fondo.setBounds(0, 0, 300, 200);
        fondo.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setOpaque(false); // Hace que el panel sea transparente para que se vea el fondo
        panel.setLayout(new GridLayout(3, 2));

        JLabel etiquetaTiradaUsuario = new JLabel("    Tu tirada:");
        etiquetaTiradaUsuario.setForeground(java.awt.Color.white);
        final JTextField campoTiradaUsuario = new JTextField(5);

        JLabel etiquetaTiradaMonstruo = new JLabel("    Tirada del Monstruo:");
        etiquetaTiradaMonstruo.setForeground(java.awt.Color.white);
        final JTextField campoTiradaMonstruo = new JTextField(5);

        JLabel etiquetaResultado = new JLabel("    Resultado");
        etiquetaResultado.setForeground(java.awt.Color.white);
        final JLabel resultado = new JLabel();
        resultado.setForeground(java.awt.Color.white);
        final JButton botonPelar = new JButton("Pelear");
        final JButton botonFinalizarTurno = new JButton("Finalizar Turno");
        botonFinalizarTurno.setEnabled(false);
        
        botonPelar.addActionListener(new ActionListener() {
        	 
            @Override
            public void actionPerformed(ActionEvent e) {
                Random random = new Random();
                int tiradaUsuario = random.nextInt(5) + 8;
                int tiradaMonstruo = random.nextInt(12) + 1;

                campoTiradaUsuario.setText(String.valueOf("                 "+tiradaUsuario));
                campoTiradaMonstruo.setText(String.valueOf("                 "+tiradaMonstruo));

                int resultadoTirada = Integer.compare(tiradaUsuario, tiradaMonstruo);
                if (resultadoTirada > 0) {
                    resultado.setText("      ¡Ganaste!");
                    botonPelar.setEnabled(false);
                    botonFinalizarTurno.setEnabled(true);
 
                } else if (resultadoTirada < 0) {
                	resultado.setText("      ¡Perdiste!");
                	botonPelar.setEnabled(false);
                	 botonFinalizarTurno.setEnabled(true);
                } else {
                    resultado.setText("      ¡Empate!");
                    
                }
               
                
            }
        });
        botonFinalizarTurno.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Inventario inv = new Inventario();
				// TODO Auto-generated method stub
				if(resultado.getText().equals("      ¡Perdiste!")) {
					dialogo.dispose();
					perdiste();	
					Gdx.app.exit();
				}else {
					dialogo.dispose();
					System.out.println("wii");					
					mostrarImagenEmergente("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\Poster\\win.png", "GANASTE", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
					Gdx.app.exit();
				}
			}
        	
        });
        
        panel.add(etiquetaTiradaUsuario);
        panel.add(campoTiradaUsuario);
        panel.add(etiquetaTiradaMonstruo);
        panel.add(campoTiradaMonstruo);
        panel.add(etiquetaResultado);
        panel.add(resultado);

        fondo.add(panel, BorderLayout.CENTER);
       
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new GridLayout(1, 2));
        panelBotones.add(botonPelar);
        panelBotones.add(botonFinalizarTurno);
        fondo.add(panelBotones, BorderLayout.SOUTH);
        dialogo.setContentPane(fondo);
        dialogo.setVisible(true);
        
       
    }

    private void perdiste() {
    	mostrarImagenEmergente("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\Poster\\MORISTE.png", "PERDISTE", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	teleport(296,238);
    }
    
    private void renderMonster(SpriteBatch batch) {
        TextureRegion currentFrame = monsterAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, monsterPosition.x, monsterPosition.y);
    }
    
    private void renderMonster2(SpriteBatch batch) {
        TextureRegion currentFrame = monsterAnimation2.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, monsterPosition2.x, monsterPosition2.y);
    }
    
    private void renderMonster3(SpriteBatch batch) {
        TextureRegion currentFrame = monsterAnimation3.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, monsterPosition3.x, monsterPosition3.y);
    }
    
    private void renderMonster4(SpriteBatch batch) {
        TextureRegion currentFrame = monsterAnimation4.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, monsterPosition4.x, monsterPosition4.y);
    }
    
    private void renderMonster5(SpriteBatch batch) {
        TextureRegion currentFrame = monsterAnimation5.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, monsterPosition5.x, monsterPosition5.y);
    }
    
    private void spawnLucius(int x, int y) {
        // Cargar las texturas del monstruo
        TextureRegion[] monsterFrames = new TextureRegion[10];
        for (int i = 0; i < 10; i++) {
            Texture texture = new Texture("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\MonstruoLucius\\frame" + (i + 1) + ".png");
            monsterFrames[i] = new TextureRegion(texture);
        }

        // Crear la animación del monstruo
        Animation<TextureRegion> monsterAnimation = new Animation<>(0.18f, monsterFrames);

        // Crear la posición del monstruo
        Vector2 monsterPosition = new Vector2(x, y);

        // Guardar las variables en los campos correspondientes
        this.monsterFrames = monsterFrames;
        this.monsterAnimation = monsterAnimation;
        this.monsterPosition = monsterPosition;
    }
    
    private void spawnBiblio(int x, int y) {
        // Cargar las texturas del monstruo
        TextureRegion[] monsterFrames2 = new TextureRegion[9];
        for (int i = 0; i < 9; i++) {
            Texture texture2 = new Texture("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\MonstruoBiblio\\Idle" + (i + 1) + ".png");
            monsterFrames2[i] = new TextureRegion(texture2);
        }

        // Crear la animación del monstruo
        Animation<TextureRegion> monsterAnimation2 = new Animation<>(0.18f, monsterFrames2);

        // Crear la posición del monstruo
        Vector2 monsterPosition2 = new Vector2(x, y);

        // Guardar las variables en los campos correspondientes
        this.monsterFrames2 = monsterFrames2;
        this.monsterAnimation2 = monsterAnimation2;
        this.monsterPosition2 = monsterPosition2;
    }
    
    private void spawnDorm(int x, int y) {
        // Cargar las texturas del monstruo
        TextureRegion[] monsterFrames3 = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            Texture texture3 = new Texture("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\MonstruoDorm\\" + (i + 1) + ".png");
            monsterFrames3[i] = new TextureRegion(texture3);
        }

        // Crear la animación del monstruo
        Animation<TextureRegion> monsterAnimation3 = new Animation<>(0.18f, monsterFrames3);

        // Crear la posición del monstruo
        Vector2 monsterPosition3 = new Vector2(x, y);

        // Guardar las variables en los campos correspondientes
        this.monsterFrames3 = monsterFrames3;
        this.monsterAnimation3 = monsterAnimation3;
        this.monsterPosition3 = monsterPosition3;
    }
    
    private void spawnCocina(int x, int y) {
        // Cargar las texturas del monstruo
        TextureRegion[] monsterFrames4 = new TextureRegion[6];
        for (int i = 0; i < 6; i++) {
            Texture texture4 = new Texture("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\MonstruoCocina\\" + (i + 1) + ".png");
            monsterFrames4[i] = new TextureRegion(texture4);
        }

        // Crear la animación del monstruo
        Animation<TextureRegion> monsterAnimation4 = new Animation<>(0.18f, monsterFrames4);

        // Crear la posición del monstruo
        Vector2 monsterPosition4 = new Vector2(x, y);

        // Guardar las variables en los campos correspondientes
        this.monsterFrames4 = monsterFrames4;
        this.monsterAnimation4 = monsterAnimation4;
        this.monsterPosition4 = monsterPosition4;
    }
    
    private void spawnFinal(int x, int y) {
        // Cargar las texturas del monstruo
        TextureRegion[] monsterFrames5 = new TextureRegion[8];
        for (int i = 0; i < 8; i++) {
            Texture texture5 = new Texture("C:\\Users\\yomar\\OneDrive\\Escritorio\\Escaperoom\\assets\\MonstruoFinal\\" + (i + 1) + ".png");
            monsterFrames5[i] = new TextureRegion(texture5);
        }

        // Crear la animación del monstruo
        Animation<TextureRegion> monsterAnimation5 = new Animation<>(0.18f, monsterFrames5);

        // Crear la posición del monstruo
        Vector2 monsterPosition5 = new Vector2(x, y);

        // Guardar las variables en los campos correspondientes
        this.monsterFrames5 = monsterFrames5;
        this.monsterAnimation5 = monsterAnimation5;
        this.monsterPosition5 = monsterPosition5;
    }

    private void changeRoom() {
    	String puerta= whichDoor();
    	System.out.println(puerta);
        MapLayer doorLayer = map.getLayers().get("puertas");
                    if (puerta.contentEquals("puertaBar")) {
                        int entryX = 186;
                        int entryY = 350;
                        teleport(entryX, entryY);                  
                        return;
                    }else   if (puerta.contentEquals("puertaCocina")) {
                            int entryX = 235;
                            int entryY = 500;
                            teleport(entryX, entryY);                  
                            return;
                    }else   if (puerta.contentEquals("puertaBar2")) {
                        int entryX = 235;
                        int entryY = 348;
                        teleport(entryX, entryY);                  
                        return;
                    }else   if (puerta.contentEquals("puertaCocina2")) {
                        int entryX = 275;
                        int entryY = 400;
                        teleport(entryX, entryY);                  
                        return;
                    }else   if (puerta.contentEquals("puertaPasillo")) {
                        int entryX = 390;
                        int entryY = 530;
                        teleport(entryX, entryY);                  
                        return;
                    }else   if (puerta.contentEquals("puertaPasillo2")) {
                        int entryX = 325;
                        int entryY = 400;
                        teleport(entryX, entryY);                  
                        return;
                    }else   if (puerta.contentEquals("puertaBiblioteca")) {
                        int entryX = 410;
                        int entryY = 380;
                        teleport(entryX, entryY);                  
                        return;
                    }else   if (puerta.contentEquals("puertaBiblioteca2")) {
                        int entryX = 360;
                        int entryY = 340;
                        teleport(entryX, entryY);                  
                        return;
                    }else   if (puerta.contentEquals("puertaCalabozo")) {
                        int entryX = 390;
                        int entryY = 790;
                        teleport(entryX, entryY);                  
                        return;
                    }else   if (puerta.contentEquals("puertaCalabozo2")) {
                    	int entryX = 460;
                        int entryY = 660;
                        teleport(entryX, entryY);                  
                        return;
                    }else   if (puerta.contentEquals("puertaCuarto")) {
                        int entryX = 585;
                        int entryY = 785;
                        teleport(entryX, entryY);                  
                        return;
                    }else   if (puerta.contentEquals("puertaCuarto2")) {
                    	int entryX = 600;
                        int entryY = 660;
                        teleport(entryX, entryY);                  
                        return;
                    }
                }    
    
    private String whichDoor() {
        MapLayer doorLayer = map.getLayers().get("puertas");

        if (doorLayer != null) {
            String closestDoor = null;
            float closestDistance = Float.MAX_VALUE;
            
            for (MapObject object : doorLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    // Calcula la distancia entre el jugador y la puerta actual
                    float distance = calculateDistance(playerPosition.x, playerPosition.y, doorX, doorY);
                    
                    // Comprueba si la distancia es menor que la más cercana hasta ahora
                    if (distance < closestDistance) {
                        closestDoor = rectangleObject.getName();
                        closestDistance = distance;
                    }
                }
            }
            
            return closestDoor;
        }

        return null; // Return null if the player is not standing on any door rectangle
    }
    private float calculateDistance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
    private String whichCajon() {
        MapLayer doorLayer = map.getLayers().get("Loot");

        if (doorLayer != null) {
            for (MapObject object : doorLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle doorRectangle = rectangleObject.getRectangle();

                    float doorX = doorRectangle.x * mapScale;
                    float doorY = doorRectangle.y * mapScale;
                    float doorWidth = doorRectangle.width * mapScale;
                    float doorHeight = doorRectangle.height * mapScale;

                    
                    if (playerPosition.x >= doorX && playerPosition.x <= doorX + doorWidth
                            && playerPosition.y >= doorY && playerPosition.y <= doorY + doorHeight) {
                        return rectangleObject.getName();
                    }
                }
            }
        }

        return null; // Return null if the player is not standing on any door rectangle
    }
    private void abrircajon() {
        Inventario inv = new Inventario();

        MapLayer lootLayer = map.getLayers().get("Loot");
        String cajon = whichCajon();

        if (lootLayer != null) {
            for (MapObject object : lootLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;

                    // Verificar si el nombre del rectángulo coincide con el nombre del cajón
                    if (rectangleObject.getName().equals(cajon)) {
                        MapProperties properties = rectangleObject.getProperties();
                        StringBuilder attributeString = new StringBuilder();

                        // Obtener solo los atributos personalizados del rectángulo
                        Iterator<String> propertyKeys = properties.getKeys();

                        while (propertyKeys.hasNext()) {
                            String key = propertyKeys.next();

                            // Verificar si el atributo es personalizado
                            if (isStandardAttribute(key)) {
                                Object value = properties.get(key, null);

                                if (value != null) {
                                    // Convertir el valor al tipo de dato correspondiente
                                    if (value instanceof Integer) {
                                        attributeString.append((Integer) value).append(",");
                                    } else if (value instanceof Float) {
                                        attributeString.append((Float) value).append(",");
                                    } else if (value instanceof Boolean) {
                                        attributeString.append((Boolean) value).append(",");
                                    } else {
                                        // Si el tipo de dato no coincide con los tipos esperados, se asume como String
                                        attributeString.append(value.toString()).append(",");
                                    }
                                }
                            }
                        }

                        // Eliminar la última coma
                        if (attributeString.length() > 0) {
                            attributeString.setLength(attributeString.length() - 1);
                        }

                        String attributes = attributeString.toString();
                        System.out.println(attributes);
                        inv.addItem(attributes);
                        mostrarMensajeEmergente("Conseguiste Llave Bar");

                        // Realizar acciones adicionales según sea necesario
                    }
                }
            }
        }
    }
    public static void mostrarImagenEmergente(String imagePath, String titulo, int ancho, int alto) {
        ImageIcon icono = new ImageIcon(imagePath);
        Image imagen = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_DEFAULT);
        ImageIcon iconoEscalado = new ImageIcon(imagen);
        JLabel etiqueta = new JLabel(iconoEscalado);
        JOptionPane.showMessageDialog(null, etiqueta, titulo, JOptionPane.PLAIN_MESSAGE);
    }
    private void abrirMaquinaExpendedora() {
        Inventario inv = new Inventario();
                        String attributes = "1,objetopocion,2,1000,5,lataMonster,false,123,123";
                        System.out.println(attributes);
                        inv.addItem(attributes);
                        mostrarMensajeEmergente("Conseguiste una lata de Monster");

                        // Realizar acciones adicionales según sea necesario
  
    }
    private void tomarTorta() {
        Inventario inv = new Inventario();
                        String attributes = "1,objetopocion,2,1000,5,pedazoTorta,false,123,123";
                        System.out.println(attributes);
                        inv.addItem(attributes);
                        mostrarMensajeEmergente("Conseguiste un pedazo de torta");

                        // Realizar acciones adicionales según sea necesario
  
    }
    private void tomaralcohol() {
        Inventario inv = new Inventario();
                        String attributes = "1,objetopocion,2,1000,5,alcohol,false,123,123";
                        System.out.println(attributes);
                        inv.addItem(attributes);
                        mostrarMensajeEmergente("Conseguiste una botella de alcohol");

                        // Realizar acciones adicionales según sea necesario
  
    }
    private boolean abrirpuerta() {
    	ArrayList <Item> objetos = Inventario.leerInventario();
    	String puerta = whichDoor();
    	System.out.println(puerta);
    	for (Item i: objetos) {
    		if (i.getNombre().equals(puerta)) {
    			System.out.println("siiii");
    			return true;
    		}
    	}return false;
    }
    
    private boolean verificarobjetos() {
        ArrayList<Item> objetos = Inventario.leerInventario();
        
        boolean lataMonsterEncontrada = false;
        boolean alcoholEncontrado = false;
        boolean pedazoTortaEncontrado = false;
        
        for (Item objeto : objetos) {
            String nombre = objeto.getNombre();
            
            if (nombre.equals("lataMonster")) {
                lataMonsterEncontrada = true;
            } else if (nombre.equals("alcohol")) {
                alcoholEncontrado = true;
            } else if (nombre.equals("pedazoTorta")) {
                pedazoTortaEncontrado = true;
            }
        }
            // Si los tres objetos han sido encontrados, no es necesario seguir iterando
            if (lataMonsterEncontrada && alcoholEncontrado && pedazoTortaEncontrado) {
            	return true;
               
            }else {
            	return false;
            }
        }   
    
    private boolean isStandardAttribute(String attribute) { 
    	System.out.println("* "+ attribute);
        List<String> standardAttributes = Arrays.asList(
            "id", "Plantilla", "Nombre", "Class", "Visible", "x", "y", "width", "height", "Rotacion"
        );

        return !standardAttributes.contains(attribute);
    }
    private void teleport(int entryX, int entryY) {
    	 entryX *= mapScale;
         entryY *= mapScale;
         playerPosition.set(entryX, entryY);
         previousPlayerPosition.set(playerPosition);
         playerRect.setPosition(playerPosition);
         updateCamera(); 
        
        }
    
    private boolean detectCollision() {
        int playerTileX = (int) (playerPosition.x / TILE_WIDTH);
        int playerTileY = (int) (playerPosition.y / TILE_HEIGHT);

        MapLayer collisionLayer = map.getLayers().get("bloqueos");

        if (collisionLayer != null) {
            for (MapObject object : collisionLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle rectangle = rectangleObject.getRectangle();

                    if (rectangle.overlaps(playerRect)) {
                        // Se encontró una colisión con el rectángulo bloqueado
                        return true;
                    }
                }
            }
        }

        return false;
    }
    private boolean detectCollisionLucius() {
        int playerTileX = (int) (playerPosition.x / TILE_WIDTH);
        int playerTileY = (int) (playerPosition.y / TILE_HEIGHT);

        MapLayer monstruoLayer = map.getLayers().get("Monstruo");

        if (monstruoLayer != null) {
            for (MapObject object : monstruoLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle rectangle = rectangleObject.getRectangle();

                    if (rectangle.overlaps(playerRect)) {
                        // Se encontró una colisión con el rectángulo bloqueado
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void updateCamera() {
        camera.position.set(playerPosition.x, playerPosition.y, 0);
        camera.update();
    }

    private void updateAnimation() {
        stateTime += Gdx.graphics.getDeltaTime();
    }

    private TextureRegion getCurrentFrame() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            return walkAnimation.getKeyFrame(stateTime, true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            return walkAnimation.getKeyFrame(stateTime, true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            return walkAnimation.getKeyFrame(stateTime, true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            return walkAnimation.getKeyFrame(stateTime, true);
        } else {
            return new TextureRegion(idleTexture);
        }
    }
}