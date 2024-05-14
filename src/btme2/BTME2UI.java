
package btme2;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import java.io.IOException; 
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 


/**
 *
 * @author barackobama
 */

public class BTME2UI extends javax.swing.JFrame {
    
    /**
     *  The name given to the map
     */
    public static String MAP_NAME = "PLACE";
    
    public static byte HEADER[] = {'B', 'T', 'M', 'v', 2, 0, 0, 0};
    
    public static LinkedList<BT_Barrier> MAP_BARRIERS = new LinkedList<>();
    public static LinkedList<BT_Entity> MAP_ENTITIES = new LinkedList<>();
    
    public static BT_Sprite Entity_Images[][] = new BT_Sprite[255][255];
    
    public static int CURRENT_MODE = 2;
    public static int CURRENT_COLOR = 5;
    public static int CURRENT_ENTITY = 1;
    public static int CURRENT_ENTITY_TYPE = 0;
    public static int IS_ON_VIEWER = 0;
    
    public static int MAP_X = 8;
    public static int MAP_Y = 8;
    
    public static int MOUSE_PRESSED_TYPE = 0;
    
    public Color COLORLIST[] = {
        Color.GRAY,
        Color.RED,
        Color.ORANGE,
        Color.YELLOW,
        Color.GREEN,
        Color.BLUE,
        Color.magenta
    };
    
    Graphics g;
    Graphics mvg;
    BufferedImage bi;
    
    Cursor amicursor;
    Image cursorimg;
    
    JFrame choose;
    JFrame about;
    JFrame namechoose;
    
    Point mousePosition;
    
    static int labelopacity = 0;
    static int labeltime = 0;
    
    private static BT_Barrier vert_wall_buffer;
    private static BT_Barrier hori_wall_buffer;
    private static BT_Object MOVE_BUFFER = null;
    private static final LinkedList<BT_Object> SELECTIONS_BUFFER = new LinkedList();
    public boolean MOUSE_IS_HELD = false;
    
    public static SELECT_TOOL_BUFFER SELECT_BUFFER = null;
    public boolean GRID_TOGGLE = false;
    public ArrayList<LinkedList<BT_Barrier>> UndoHistoryBarrier = new ArrayList<LinkedList<BT_Barrier>>();
    public LinkedList UndoHistoryEntities[] = { MAP_ENTITIES };
    public int undoIndex = 0;
    public int lastTimeTravel = 2; //0-Undo, 1-Redo, 2-None
    
    /**
     * Creates new form BTME2UI
     */
    public BTME2UI() {
        // SET THE THEME
        try {
                // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

        } 
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            try {
                // handle exception
                UIManager.setLookAndFeel ("javax.swing.plaf.metal.MetalLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                try {
                    // handle exception
                    UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex2) {
                    Logger.getLogger(BTME2UI.class.getName()).log(Level.SEVERE, null, ex2);
                }
            }
        }
        UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo look : looks) {
            System.out.println(look.getClassName());
        }
        
         //making shit NOT grey
         UIManager.put("ToggleButton.select", new Color(67, 52, 163));
         UIManager.put("Button.select", new Color(67, 52, 163));
        
        // netbeans sstuff
        initComponents();
        
        // i forgot what this does
        bi = new BufferedImage(24 * 480, 24 * 360, BufferedImage.TYPE_INT_RGB);
        g = bi.createGraphics();
        mvg = map_view_panel.getGraphics();
        
        // music playback
        try 
        {
            UI_BGM audioPlayer = new UI_BGM();
            audioPlayer.clip.start();
        }
        catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex)
        {
            System.out.println("Something went wrong with Audio Playback");
        }
        
        // set the window icon
        Image iconimg;
        iconimg = new ImageIcon(this.getClass().getResource("/icon.png")).getImage();
        try {
            String filepath;
            filepath = "/btme2/btron-assets/btron_b3.png";
            Entity_Images[0][0] = new BT_Sprite(ImageIO.read(getClass().getResource(filepath)), 4, 12);
            
            filepath = "btron-assets/btron_camera.png";
            Entity_Images[1][0] = new BT_Sprite(ImageIO.read(getClass().getResource(filepath)));
            
            filepath = "btron-assets/btron-verticaldoor-default.png";
            Entity_Images[2][0] = new BT_Sprite(ImageIO.read(getClass().getResource(filepath)));
            
            filepath = "btron-assets/btron-verticaldoor-red.png";
            Entity_Images[2][1] = new BT_Sprite(ImageIO.read(getClass().getResource(filepath)));
            
            filepath = "btron-assets/btron-verticaldoor-green.png";
            Entity_Images[2][2] = new BT_Sprite(ImageIO.read(getClass().getResource(filepath)));
            
            filepath = "btron-assets/btron-verticaldoor-blue.png";
            Entity_Images[2][3] = new BT_Sprite(ImageIO.read(getClass().getResource(filepath)));
            
            filepath = "btron-assets/btron-verticaldoor-yellow.png";
            Entity_Images[2][4] = new BT_Sprite(ImageIO.read(getClass().getResource(filepath)));
            
            filepath = "btron-assets/btron-verticaldoor-purple.png";
            Entity_Images[2][5] = new BT_Sprite(ImageIO.read(getClass().getResource(filepath)));
            
            filepath = "btron-assets/btron-key-default.png";
            Entity_Images[4][0] = new BT_Sprite(ImageIO.read(getClass().getResource(filepath)));
            
            filepath = "btron-assets/btron-key-red.png";
            Entity_Images[4][1] = new BT_Sprite(ImageIO.read(getClass().getResource(filepath)));
            
            filepath = "btron-assets/btron-key-green.png";
            Entity_Images[4][2] = new BT_Sprite(ImageIO.read(getClass().getResource(filepath)));
            
            filepath = "btron-assets/btron-key-blue.png";
            Entity_Images[4][3] = new BT_Sprite(ImageIO.read(getClass().getResource(filepath)));
            
            filepath = "btron-assets/btron-key-yellow.png";
            Entity_Images[4][4] = new BT_Sprite(ImageIO.read(getClass().getResource(filepath)));
            
            
        } catch (IOException ex) {
            Logger.getLogger(BTME2UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // set the cursor
        cursorimg = new ImageIcon(this.getClass().getResource("/cursor.png")).getImage();
        amicursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorimg, new Point(0,0), "Amiga");
        setCursor(amicursor);
        
        // set popup windows
        about = new BTME2_About();
        choose = new ObjectChooser();
        namechoose = new NameEntry();
        
        
        this.setIconImage(iconimg);
        this.setLocationRelativeTo(null);
        
        jLabel3.setForeground(new Color (255, 255, 255, labelopacity));
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            
            @Override
            public void run() {
                decreasealertopacity();
                clear_screen();            
            }
            
        }, 0, 1);
        
        
    }
    
    public void decreasealertopacity()
    {
        labeltime--;
        if (labeltime > 0 || labelopacity == 0) {return;}
        labelopacity--;
        jLabel3.setForeground(new Color (255, 255, 255, labelopacity));
    }
    
    public void addundostate()
    {
        if (lastTimeTravel < 2) //user has fucked up our timeline; let's fix it!
        {
            if (lastTimeTravel == 0) {
            UndoHistoryBarrier.subList(undoIndex + 1, UndoHistoryBarrier.size()).clear(); //undoIndex will always be 1 less than what we're on
            }
            if (lastTimeTravel == 1) {
            UndoHistoryBarrier.subList(undoIndex, UndoHistoryBarrier.size()).clear(); //undoIndex will be what we're on
            }
        }
        if (UndoHistoryBarrier.size() - 1 != -1 && lastTimeTravel == 2) //we ALREADY removed current state if last move was re/undo :3
        {
        UndoHistoryBarrier.remove(UndoHistoryBarrier.size() - 1); //removing current state, no longer needed :D
        }
        UndoHistoryBarrier.add((LinkedList) MAP_BARRIERS.clone());
        undo.setEnabled(true);
    }
    
    public void addundopost()
    {
        UndoHistoryBarrier.add((LinkedList) MAP_BARRIERS.clone());
        undoIndex = UndoHistoryBarrier.size() - 2;
        System.out.println(undoIndex);
        if (lastTimeTravel == 0)
        {
        lastTimeTravel = 2;
        redoFunction(); //shhhh don't let them know about the jank...
        return;
        }
        lastTimeTravel = 2;
    }
    
    public void undoFunction()
    {
        if (lastTimeTravel == 1)
        {
            undoIndex--;
        }
        jMenuItem1.setEnabled(true); //if we undid something, there has to be something to redo
        MAP_BARRIERS = UndoHistoryBarrier.get(undoIndex);
        if (undoIndex - 1 != -1)
        {
            undoIndex--;
        }
        else
        {
            undoIndex--;
            undo.setEnabled(false);
        }
        System.out.println(undoIndex);
        lastTimeTravel = 0;
    }
    
    public void redoFunction()
    {
        undoIndex++;
        if (lastTimeTravel == 0)
        {
            undoIndex++;
        }
        undo.setEnabled(true); //if we redid something, there has to be something to undo
        System.out.println(undoIndex);
        MAP_BARRIERS = UndoHistoryBarrier.get(undoIndex);
        System.out.println(MAP_BARRIERS);
        if (undoIndex + 1 > UndoHistoryBarrier.size() - 1)
        {
            jMenuItem1.setEnabled(false);
        }
        lastTimeTravel = 1;
    }
    
    public static void changealert(String message)
    {
        BTME2UI.labelopacity = 255;
        BTME2UI.labeltime = 1024;
        jLabel3.setText(message);
        jLabel3.setForeground(new Color (255, 255, 255, labelopacity));
    }
    
    private void changeBorders() {                                     
        btn_select.setBorder(javax.swing.BorderFactory.createBevelBorder((CURRENT_MODE == 0) ? 1 : 0));
        btn_pencil.setBorder(javax.swing.BorderFactory.createBevelBorder((CURRENT_MODE == 2) ? 1 : 0));
        btn_delete.setBorder(javax.swing.BorderFactory.createBevelBorder((CURRENT_MODE == 3) ? 1 : 0));
        btn_move.setBorder(javax.swing.BorderFactory.createBevelBorder((CURRENT_MODE == 4) ? 1 : 0));
        btn_stamp.setBorder(javax.swing.BorderFactory.createBevelBorder((CURRENT_MODE == 5) ? 1 : 0));
        btn_vertline.setBorder(javax.swing.BorderFactory.createBevelBorder((CURRENT_MODE == 6) ? 1 : 0));
        btn_horiline.setBorder(javax.swing.BorderFactory.createBevelBorder((CURRENT_MODE == 7) ? 1 : 0));
    } 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btn_select = new javax.swing.JToggleButton();
        btn_objects = new javax.swing.JButton();
        btn_pencil = new javax.swing.JToggleButton();
        btn_delete = new javax.swing.JToggleButton();
        btn_move = new javax.swing.JToggleButton();
        btn_stamp = new javax.swing.JToggleButton();
        btn_vertline = new javax.swing.JToggleButton();
        btn_horiline = new javax.swing.JToggleButton();
        btn_name = new javax.swing.JButton();
        map_view_panel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        down = new javax.swing.JButton();
        left = new javax.swing.JButton();
        right = new javax.swing.JButton();
        up = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        menubar = new javax.swing.JMenuBar();
        file = new javax.swing.JMenu();
        importmap = new javax.swing.JMenuItem();
        exportmap = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        edit = new javax.swing.JMenu();
        undo = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("B-TRON MAP EDITOR 2");
        setAutoRequestFocus(false);
        setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        setForeground(java.awt.Color.white);
        setMaximumSize(new java.awt.Dimension(720, 540));
        setMinimumSize(new java.awt.Dimension(720, 540));
        setName("B-TRON MAP EDITOR 2"); // NOI18N
        setResizable(false);
        setSize(new java.awt.Dimension(720, 540));

        jPanel1.setBackground(new java.awt.Color(0, 0, 153));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setCursor(amicursor);
        jPanel1.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        jPanel1.setMaximumSize(new java.awt.Dimension(720, 540));
        jPanel1.setMinimumSize(new java.awt.Dimension(720, 540));
        jPanel1.setPreferredSize(new java.awt.Dimension(720, 540));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel1MouseEntered(evt);
            }
        });
        jPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel1KeyPressed(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(1, 1));

        btn_select.setBackground(new java.awt.Color(153, 153, 255));
        buttonGroup2.add(btn_select);
        btn_select.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/select.png"))); // NOI18N
        btn_select.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_select.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_select.setFocusPainted(false);
        btn_select.setMaximumSize(new java.awt.Dimension(32, 32));
        btn_select.setMinimumSize(new java.awt.Dimension(32, 32));
        btn_select.setPreferredSize(new java.awt.Dimension(32, 32));
        btn_select.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_selectMouseClicked(evt);
            }
        });
        jPanel2.add(btn_select);

        btn_objects.setBackground(new java.awt.Color(153, 153, 255));
        btn_objects.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/objects.png"))); // NOI18N
        btn_objects.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buttonGroup2.add(btn_objects);
        btn_objects.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_objects.setFocusPainted(false);
        btn_objects.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btn_objects.setMaximumSize(new java.awt.Dimension(32, 32));
        btn_objects.setMinimumSize(new java.awt.Dimension(32, 32));
        btn_objects.setPreferredSize(new java.awt.Dimension(32, 32));
        btn_objects.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_objectsActionPerformed(evt);
            }
        });
        jPanel2.add(btn_objects);

        btn_pencil.setBackground(new java.awt.Color(153, 153, 255));
        buttonGroup2.add(btn_pencil);
        btn_pencil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/penis.png"))); // NOI18N
        btn_pencil.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        btn_pencil.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_pencil.setFocusPainted(false);
        btn_pencil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pencilActionPerformed(evt);
            }
        });
        jPanel2.add(btn_pencil);

        btn_delete.setBackground(new java.awt.Color(153, 153, 255));
        buttonGroup2.add(btn_delete);
        btn_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/delete.png"))); // NOI18N
        btn_delete.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_delete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_delete.setFocusPainted(false);
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });
        jPanel2.add(btn_delete);

        btn_move.setBackground(new java.awt.Color(153, 153, 255));
        buttonGroup2.add(btn_move);
        btn_move.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/move.png"))); // NOI18N
        btn_move.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_move.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_move.setFocusPainted(false);
        btn_move.setMaximumSize(new java.awt.Dimension(32, 32));
        btn_move.setMinimumSize(new java.awt.Dimension(32, 32));
        btn_move.setPreferredSize(new java.awt.Dimension(32, 32));
        btn_move.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_moveActionPerformed(evt);
            }
        });
        jPanel2.add(btn_move);

        btn_stamp.setBackground(new java.awt.Color(153, 153, 255));
        buttonGroup2.add(btn_stamp);
        btn_stamp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/stamp.png"))); // NOI18N
        btn_stamp.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_stamp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_stamp.setFocusPainted(false);
        btn_stamp.setMaximumSize(new java.awt.Dimension(32, 32));
        btn_stamp.setMinimumSize(new java.awt.Dimension(32, 32));
        btn_stamp.setPreferredSize(new java.awt.Dimension(32, 32));
        btn_stamp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_stampActionPerformed(evt);
            }
        });
        jPanel2.add(btn_stamp);

        btn_vertline.setBackground(new java.awt.Color(153, 153, 255));
        buttonGroup2.add(btn_vertline);
        btn_vertline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/vert-line.png"))); // NOI18N
        btn_vertline.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_vertline.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_vertline.setFocusPainted(false);
        btn_vertline.setMaximumSize(new java.awt.Dimension(32, 32));
        btn_vertline.setMinimumSize(new java.awt.Dimension(32, 32));
        btn_vertline.setPreferredSize(new java.awt.Dimension(32, 32));
        btn_vertline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_vertlineActionPerformed(evt);
            }
        });
        jPanel2.add(btn_vertline);

        btn_horiline.setBackground(new java.awt.Color(153, 153, 255));
        buttonGroup2.add(btn_horiline);
        btn_horiline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/hori-line.png"))); // NOI18N
        btn_horiline.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_horiline.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_horiline.setFocusPainted(false);
        btn_horiline.setMaximumSize(new java.awt.Dimension(32, 32));
        btn_horiline.setMinimumSize(new java.awt.Dimension(32, 32));
        btn_horiline.setPreferredSize(new java.awt.Dimension(32, 32));
        btn_horiline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_horilineActionPerformed(evt);
            }
        });
        jPanel2.add(btn_horiline);

        btn_name.setBackground(new java.awt.Color(153, 153, 255));
        btn_name.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/text.png"))); // NOI18N
        btn_name.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buttonGroup2.add(btn_name);
        btn_name.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_name.setFocusPainted(false);
        btn_name.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btn_name.setMaximumSize(new java.awt.Dimension(32, 32));
        btn_name.setMinimumSize(new java.awt.Dimension(32, 32));
        btn_name.setPreferredSize(new java.awt.Dimension(32, 32));
        btn_name.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_nameMouseClicked(evt);
            }
        });
        btn_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nameActionPerformed(evt);
            }
        });
        jPanel2.add(btn_name);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 30, 288, -1));

        map_view_panel.setBackground(new java.awt.Color(0, 0, 0));
        map_view_panel.setForeground(new java.awt.Color(255, 255, 255));
        map_view_panel.setMaximumSize(new java.awt.Dimension(480, 360));
        map_view_panel.setMinimumSize(new java.awt.Dimension(480, 360));
        map_view_panel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                map_view_panelMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                map_view_panelMouseMoved(evt);
            }
        });
        map_view_panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                map_view_panelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                map_view_panelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                map_view_panelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                map_view_panelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout map_view_panelLayout = new javax.swing.GroupLayout(map_view_panel);
        map_view_panel.setLayout(map_view_panelLayout);
        map_view_panelLayout.setHorizontalGroup(
            map_view_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );
        map_view_panelLayout.setVerticalGroup(
            map_view_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );

        jPanel1.add(map_view_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, -1, -1));

        jLabel1.setFont(new java.awt.Font("BigBlue Terminal 437TT", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Border Color");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 440, 110, -1));

        jComboBox1.setFont(new java.awt.Font("BigBlue Terminal 437TT", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Black", "Red", "Orange", "Yellow", "Green", "Blue", "Violet" }));
        jComboBox1.setSelectedIndex(5);
        jComboBox1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jComboBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox1.setRequestFocusEnabled(false);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 460, 110, -1));

        jLabel2.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 255));
        jLabel2.setText("Version 2.0.0 - NOMAD");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, -1, -1));

        jLabel3.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Propo", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Initialized");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 490, -1, -1));

        jLabel4.setFont(new java.awt.Font("BigBlueTermPlus Nerd Font", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("B-TRON MAP EDITOR");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, -1));

        down.setBackground(new java.awt.Color(153, 153, 255));
        down.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/down.png"))); // NOI18N
        down.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        down.setMargin(new java.awt.Insets(0, 0, 0, 0));
        down.setMaximumSize(new java.awt.Dimension(32, 32));
        down.setMinimumSize(new java.awt.Dimension(32, 32));
        down.setPreferredSize(new java.awt.Dimension(32, 32));
        down.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downActionPerformed(evt);
            }
        });
        jPanel1.add(down, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 110, -1, -1));

        left.setBackground(new java.awt.Color(153, 153, 255));
        left.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/left.png"))); // NOI18N
        left.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        left.setMargin(new java.awt.Insets(0, 0, 0, 0));
        left.setMaximumSize(new java.awt.Dimension(32, 32));
        left.setMinimumSize(new java.awt.Dimension(32, 32));
        left.setPreferredSize(new java.awt.Dimension(32, 32));
        left.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftActionPerformed(evt);
            }
        });
        jPanel1.add(left, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 440, -1, -1));

        right.setBackground(new java.awt.Color(153, 153, 255));
        right.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/right.png"))); // NOI18N
        right.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        right.setMargin(new java.awt.Insets(0, 0, 0, 0));
        right.setMaximumSize(new java.awt.Dimension(32, 32));
        right.setMinimumSize(new java.awt.Dimension(32, 32));
        right.setPreferredSize(new java.awt.Dimension(32, 32));
        right.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightActionPerformed(evt);
            }
        });
        jPanel1.add(right, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 440, -1, -1));

        up.setBackground(new java.awt.Color(153, 153, 255));
        up.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/up.png"))); // NOI18N
        up.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        up.setMargin(new java.awt.Insets(0, 0, 0, 0));
        up.setMaximumSize(new java.awt.Dimension(32, 32));
        up.setMinimumSize(new java.awt.Dimension(32, 32));
        up.setPreferredSize(new java.awt.Dimension(32, 32));
        up.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upActionPerformed(evt);
            }
        });
        jPanel1.add(up, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 70, -1, -1));

        jLabel6.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Y - 8");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        jLabel5.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("X - 8");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        menubar.setBackground(new java.awt.Color(102, 102, 255));
        menubar.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Propo", 0, 12)); // NOI18N
        menubar.setName("Menu"); // NOI18N

        file.setText("File");
        file.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        file.setFont(new java.awt.Font("BigBlue Terminal 437TT", 0, 12)); // NOI18N

        importmap.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        importmap.setFont(new java.awt.Font("BigBlue Terminal 437TT", 0, 12)); // NOI18N
        importmap.setText("<HTML><U>I</U>mport Map</HTML>");
        importmap.setToolTipText("");
        importmap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importmapActionPerformed(evt);
            }
        });
        file.add(importmap);

        exportmap.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        exportmap.setFont(new java.awt.Font("BigBlue Terminal 437TT", 0, 12)); // NOI18N
        exportmap.setText("<HTML><U>E</U>xport Map</HTML>");
        exportmap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportmapActionPerformed(evt);
            }
        });
        file.add(exportmap);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem4.setFont(new java.awt.Font("BigBlue Terminal 437TT", 0, 12)); // NOI18N
        jMenuItem4.setText("About");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        file.add(jMenuItem4);

        menubar.add(file);

        edit.setForeground(new java.awt.Color(0, 0, 102));
        edit.setText("Edit");
        edit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        edit.setFont(new java.awt.Font("BigBlue Terminal 437TT", 0, 12)); // NOI18N

        undo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        undo.setFont(new java.awt.Font("BigBlue Terminal 437TT", 0, 12)); // NOI18N
        undo.setToolTipText("Undo");
        undo.setEnabled(false);
        undo.setLabel("Undo");
        undo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoActionPerformed(evt);
            }
        });
        edit.add(undo);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem1.setFont(new java.awt.Font("BigBlue Terminal 437TT", 0, 12)); // NOI18N
        jMenuItem1.setText("Redo");
        jMenuItem1.setEnabled(false);
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        edit.add(jMenuItem1);

        menubar.add(edit);

        jMenu1.setForeground(new java.awt.Color(0, 0, 102));
        jMenu1.setText("View");
        jMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu1.setFont(new java.awt.Font("BigBlue Terminal 437TT", 0, 12)); // NOI18N

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setFont(new java.awt.Font("BigBlue Terminal 437TT", 0, 12)); // NOI18N
        jMenuItem2.setText("Toggle Grid");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        menubar.add(jMenu1);

        setJMenuBar(menubar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void undoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoActionPerformed
        undoFunction();
        changealert("Undid");
    }//GEN-LAST:event_undoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        redoFunction();
        changealert("Redid");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseEntered

    }//GEN-LAST:event_jPanel1MouseEntered

    private boolean is_barrier_hitting_mouse(BT_Barrier barr)
    {
        return (    (barr.x + barr.width > ((mousePosition.x / 8) * 8)) && 
                    (barr.x < ((mousePosition.x / 8) * 8) + 8) && 
                    (barr.y + barr.height > ((mousePosition.y / 8) * 8)) && 
                    (barr.y < ((mousePosition.y / 8) * 8) + 8)
               );
    }
    
    public boolean canbeplaced()
    {
        boolean canplace = true;
        for (int i = 0; i < MAP_BARRIERS.size(); i++)
        {
            BT_Barrier barr = MAP_BARRIERS.get(i);
            canplace = !(is_barrier_hitting_mouse(barr));
            if (canplace == false) {return false;}

        }
        return true;
    }
    
    public boolean canbeplaced_entity()
    {
        boolean canplace = true;
        for (int i = 0; i < MAP_ENTITIES.size(); i++)
        {
            BT_Entity enti = MAP_ENTITIES.get(i);
            int entiwidth = Entity_Images[enti.id][enti.type].image.getWidth();
            int entiheight = Entity_Images[enti.id][enti.type].image.getHeight();
            canplace = !((enti.x + entiwidth > mousePosition.x) && (enti.x < mousePosition.x) && (enti.y + entiheight > mousePosition.y) && (enti.y < mousePosition.y));
            if (canplace == false) {return false;}

        }
        return true;
    }
    
    private void map_view_panelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_map_view_panelMousePressed
 
        MOUSE_PRESSED_TYPE = evt.getButton();
        
        switch (CURRENT_MODE)
        {
            
            case 5 -> {
                boolean canplace = canbeplaced_entity();
                if (canplace == true)
                {
                    MAP_ENTITIES.add(new BT_Entity((mousePosition.x/8)*8, (mousePosition.y/8)*8, CURRENT_ENTITY, CURRENT_ENTITY_TYPE));
                    System.out.println("ENTITY ADDED");
                }
                else
                {
                    System.out.println("ENTITY ALREADY EXISTS HERE");
                }
            }
            
            case 6 ->
            {
                boolean canplace = canbeplaced();
                if (canplace && MOUSE_PRESSED_TYPE != 3)
                {
                    if (vert_wall_buffer == null)
                    {
                        vert_wall_buffer = new BT_Barrier((mousePosition.x/8)*8, (mousePosition.y/8)*8, 8, 8, CURRENT_COLOR);
                    }
                }
            }
            
            case 7 ->
            {
                boolean canplace = canbeplaced();
                if (canplace && MOUSE_PRESSED_TYPE != 3)
                {
                    if (hori_wall_buffer == null)
                    {
                        hori_wall_buffer = new BT_Barrier((mousePosition.x/8)*8, (mousePosition.y/8)*8, 8, 8, CURRENT_COLOR);
                    }
                }

            }

            
        }
        
        MOUSE_IS_HELD = true;
    }//GEN-LAST:event_map_view_panelMousePressed

    private void map_view_panelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_map_view_panelMouseExited
        // TODO add your handling code here:
        vert_wall_buffer = null;
        hori_wall_buffer = null;
        MOVE_BUFFER = null;
        MOUSE_IS_HELD = false;
    }//GEN-LAST:event_map_view_panelMouseExited

    private void map_view_panelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_map_view_panelMouseEntered
        switch (CURRENT_MODE)
        {
            
            case 4 -> {
            map_view_panel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }
            
            default -> {
            map_view_panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }
    }//GEN-LAST:event_map_view_panelMouseEntered

    private void map_view_panelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_map_view_panelMouseMoved
        // TODO add your handling code here:

    }//GEN-LAST:event_map_view_panelMouseMoved

    private void btn_pencilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pencilActionPerformed
        System.out.println("PENCIL TOOL SELECTED");        // TODO add your handling code here:
        changealert("Pencil Tool Selected!");
        CURRENT_MODE = 2;
        changeBorders();
    }//GEN-LAST:event_btn_pencilActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        System.out.println("DELETE TOOL SELECTED"); // TODO add your handling code here:
        changealert("Delete Tool Selected!");
        CURRENT_MODE = 3;
        changeBorders();
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_selectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_selectMouseClicked
        System.out.println("SELECT TOOL SELECTED");
        changealert("Select Tool Selected!");
        CURRENT_MODE = 0; 
        changeBorders();
    }//GEN-LAST:event_btn_selectMouseClicked

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        CURRENT_COLOR = jComboBox1.getSelectedIndex();        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void btn_objectsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_objectsActionPerformed
        choose.setLocationRelativeTo(null);
        choose.setVisible(true);
    }//GEN-LAST:event_btn_objectsActionPerformed

    private void btn_nameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_nameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_nameMouseClicked

    private void btn_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nameActionPerformed
        namechoose.setLocationRelativeTo(null);
        namechoose.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_btn_nameActionPerformed

    private void btn_moveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_moveActionPerformed
        System.out.println("MOVE TOOL SELECTED");
        changealert("Move Tool Selected!");
        CURRENT_MODE = 4;
        changeBorders();
    }//GEN-LAST:event_btn_moveActionPerformed

    private void btn_stampActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_stampActionPerformed
        System.out.println("STAMP TOOL SELECTED");
        changealert("Stamp Tool Selected!");
        CURRENT_MODE = 5;
        changeBorders();
    }//GEN-LAST:event_btn_stampActionPerformed

    private void btn_vertlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vertlineActionPerformed
        System.out.println("VERTICAL LINE TOOL SELECTED");
        changealert("Vertical Line Tool Selected!");
        CURRENT_MODE = 6; 
        changeBorders();
    }//GEN-LAST:event_btn_vertlineActionPerformed

    private void btn_horilineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_horilineActionPerformed
        System.out.println("HORIZONTAL LINE TOOL SELECTED");
        CURRENT_MODE = 7;
        changeBorders();
    }//GEN-LAST:event_btn_horilineActionPerformed

    private void map_view_panelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_map_view_panelMouseReleased
        switch (CURRENT_MODE)
        {
            case 6 ->
            {
                if (vert_wall_buffer != null && vert_wall_buffer.height > 0)
                {
                    addundostate();
                    MAP_BARRIERS.add(vert_wall_buffer);
                    vert_wall_buffer = null;
                    System.out.println("WALL ADDED");
                    addundopost();
                }
                else
                {
                    vert_wall_buffer = null;
                }
            }
            
            case 7 ->
            {
                if (hori_wall_buffer != null && hori_wall_buffer.width > 0)
                {
                    addundostate();
                    MAP_BARRIERS.add(hori_wall_buffer);
                    hori_wall_buffer = null;
                    addundopost();
                    System.out.println("WALL ADDED");
                }
                else
                {
                    hori_wall_buffer = null;
                }
            }

        }
        
        MOUSE_IS_HELD = false;
        
    }//GEN-LAST:event_map_view_panelMouseReleased

    private void map_view_panelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_map_view_panelMouseDragged
            
    }//GEN-LAST:event_map_view_panelMouseDragged

    private void exportmapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportmapActionPerformed
        try {
            
            File oldsavedmap = new File(MAP_NAME + ".btm"); 
            if (oldsavedmap.delete()) { 
              System.out.println("Deleted the file: " + oldsavedmap.getName());
            } else {
              System.out.println("Failed to delete the file: " + oldsavedmap.getName());
            } 
            
            FileOutputStream savedmap = new FileOutputStream(MAP_NAME + ".btm");
            savedmap.write(HEADER);
            
            int amountOfBarrier = MAP_BARRIERS.size();
            savedmap.write(amountOfBarrier >>> 8);
            savedmap.write(amountOfBarrier);
            
            int amountOfEntity = MAP_ENTITIES.size();
            savedmap.write(amountOfEntity >>> 8);
            savedmap.write(amountOfEntity);
            
            savedmap.write(MAP_NAME.length());
            
            // WRITE THE NAME OF THE MAP
            for (int i = 0; i < 16; i++)
            {
                if (i < MAP_NAME.length()){
                    savedmap.write(MAP_NAME.charAt(i));
                }
            }
            
            for (int i = 0; i < MAP_BARRIERS.size(); i++)
            {
                savedmap.write(((MAP_BARRIERS.get(i).x) / 8) >>> 8);
                savedmap.write((MAP_BARRIERS.get(i).x) / 8);
                
                savedmap.write(((MAP_BARRIERS.get(i).y) / 8) >>> 8);
                savedmap.write((MAP_BARRIERS.get(i).y) / 8);
                
                savedmap.write(((MAP_BARRIERS.get(i).width) / 8) >>> 8);
                savedmap.write((MAP_BARRIERS.get(i).width) / 8);
                
                savedmap.write(((MAP_BARRIERS.get(i).height) / 8) >>> 8);
                savedmap.write((MAP_BARRIERS.get(i).height) / 8);
                
                savedmap.write(MAP_BARRIERS.get(i).color);
            }
            
            for (int i = 0; i < MAP_ENTITIES.size(); i++)
            {
                savedmap.write(((MAP_ENTITIES.get(i).x) / 8) >>> 8);
                savedmap.write((MAP_ENTITIES.get(i).x) / 8);
                
                savedmap.write(((MAP_ENTITIES.get(i).y) / 8) >>> 8);
                savedmap.write((MAP_ENTITIES.get(i).y) / 8);
                
                savedmap.write(MAP_ENTITIES.get(i).id);
                savedmap.write(MAP_ENTITIES.get(i).type);
                
            }
            
            savedmap.close();
            System.out.println("Map Exported Successfully!");
            changealert("Map Exported!");
        } catch (IOException ex) {
            Logger.getLogger(BTME2UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_exportmapActionPerformed

    private void jPanel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel1KeyPressed
        
    }//GEN-LAST:event_jPanel1KeyPressed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        GRID_TOGGLE = !GRID_TOGGLE;
        changealert("Grid Toggled!");
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        about.setLocationRelativeTo(null);
        about.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void importmapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importmapActionPerformed
        JFileChooser filechooser = new JFileChooser();
        filechooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = filechooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = filechooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            
            try {
                // LOAD THE MAP
                byte[] mapcontents = Files.readAllBytes(selectedFile.toPath());
                
                if (mapcontents.length < HEADER.length)
                {
                    System.out.println("HEADER DOES NOT MATCH");
                    BTME2UI.changealert("Invalid File");
                    return;
                }
                
                for (int i = 0; i < BTME2UI.HEADER.length; i++)
                {
                    if (mapcontents[i] != BTME2UI.HEADER[i])
                    {
                        System.out.println("HEADER DOES NOT MATCH");
                        BTME2UI.changealert("Invalid File");
                        return;
                    }
                }
                
                int barrier_amount, entity_amount, name_length,
                    barrier_offset, entity_offset;
                
                barrier_amount = mapcontents[9] + (mapcontents[8] << 8);

                entity_amount = mapcontents[11] + (mapcontents[10] << 8);
                
                name_length = mapcontents[12];
                
                barrier_offset = 13 + name_length;
                
                entity_offset = barrier_offset + (barrier_amount * 9);
                
                MAP_BARRIERS.clear();
                MAP_ENTITIES.clear();
                MAP_NAME = "";
                
                byte[] namearr = new byte[name_length];
                
                
                for (int i = 0; i < (name_length); i++)
                {
                    namearr[i] = mapcontents[13 + i];
                }
                
                MAP_NAME = new String(namearr, StandardCharsets.UTF_8);
                NameEntry.mapnameentry.setText(BTME2UI.MAP_NAME);
                
                // LOAD BARRIERS
                
                for (int i = 0; i < barrier_amount; i++)
                {
                    int addx = 8 * mapcontents[barrier_offset + (i * 9) + 1] + (mapcontents[barrier_offset + (i * 9)] << 8);
                    int addy = 8 * mapcontents[barrier_offset + (i * 9) + 3] + (mapcontents[barrier_offset + (i * 9) + 2] << 8);
                    int addw = 8 * mapcontents[barrier_offset + (i * 9) + 5] + (mapcontents[barrier_offset + (i * 9) + 4] << 8);
                    int addh = 8 * mapcontents[barrier_offset + (i * 9) + 7] + (mapcontents[barrier_offset + (i * 9) + 6] << 8);
                    int addc = mapcontents[barrier_offset + (i * 9) + 8];
                    MAP_BARRIERS.add(new BT_Barrier(addx, addy, addw, addh, addc));
                }
                
                // LOAD ENTITIES
                
                for (int i = 0; i < entity_amount; i++)
                {
                    int addx = 8 * (mapcontents[entity_offset + (i * 6) + 1] + (mapcontents[entity_offset + (i * 6)] << 8));
                    int addy = 8 * (mapcontents[entity_offset + (i * 6) + 3] + (mapcontents[entity_offset + (i * 6) + 2] << 8));
                    int addi = mapcontents[entity_offset + (i * 6) + 4];
                    int addt = mapcontents[entity_offset + (i * 6) + 5];
                    MAP_ENTITIES.add(new BT_Entity(addx, addy, addi, addt));
                }
                
                System.out.println("LOADED MAP!");
                BTME2UI.changealert("Map Loaded: " + MAP_NAME);
                
            } catch (IOException ex) {
                Logger.getLogger(BTME2UI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }//GEN-LAST:event_importmapActionPerformed

    private void rightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightActionPerformed
        if (MAP_X + 1 < 24)
        {
            MAP_X++;
        }
        jLabel5.setText("X - " + MAP_X);
    }//GEN-LAST:event_rightActionPerformed

    private void leftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftActionPerformed
        if (MAP_X - 1 >= 0)
        {
            MAP_X--;
        }
        jLabel5.setText("X - " + MAP_X);
    }//GEN-LAST:event_leftActionPerformed

    private void upActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upActionPerformed
        if (MAP_Y - 1 >= 0)
        {
            MAP_Y--;
        }
        jLabel6.setText("Y - " + MAP_Y);
    }//GEN-LAST:event_upActionPerformed

    private void downActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downActionPerformed
        if (MAP_Y + 1 < 24)
        {
            MAP_Y++;
        }
        jLabel6.setText("Y - " + MAP_Y);
    }//GEN-LAST:event_downActionPerformed

    private boolean is_barrier_hitting_selection(BT_Barrier barr)
    {
        return (SELECT_BUFFER != null &&
                    (barr.x + barr.width > SELECT_BUFFER.startx) && 
                    (barr.x < SELECT_BUFFER.startx + SELECT_BUFFER.width) && 
                    (barr.y + barr.height > SELECT_BUFFER.starty) && 
                    (barr.y < SELECT_BUFFER.starty + SELECT_BUFFER.height)  
                );
    }
    
    private boolean is_entity_hitting_selection(BT_Entity enti)
    {
        int entiwidth = Entity_Images[enti.id][enti.type].image.getWidth();
        int entiheight = Entity_Images[enti.id][enti.type].image.getHeight();
        int entioriginx = Entity_Images[enti.id][enti.type].x;
        int entioriginy = Entity_Images[enti.id][enti.type].y;
        return (SELECT_BUFFER != null &&
                    (enti.x + entiwidth - entioriginx > SELECT_BUFFER.startx) && 
                    (enti.x - entioriginx < SELECT_BUFFER.startx + SELECT_BUFFER.width) && 
                    (enti.y + entiheight - entioriginy > SELECT_BUFFER.starty) && 
                    (enti.y - entioriginy < SELECT_BUFFER.starty + SELECT_BUFFER.height)  
                );
    }
    
    private void draw_viewer()
    {
        if (MOUSE_IS_HELD && MOVE_BUFFER != null)
        {
            MOVE_BUFFER.x = (mousePosition.x / 8) * 8;
            MOVE_BUFFER.y = (mousePosition.y / 8) * 8;
        }
        
        if (!MOUSE_IS_HELD && MOVE_BUFFER != null)
        {
            MOVE_BUFFER = null;
        }
        
        for (int i = 0; i < MAP_BARRIERS.size(); i++)
        {
            BT_Barrier barr = MAP_BARRIERS.get(i);
            switch (barr.color)
            {
                case 0 -> {g.setColor(Color.GRAY);}
                case 1 -> {g.setColor(Color.RED);}
                case 2 -> {g.setColor(Color.ORANGE);}
                case 3 -> {g.setColor(Color.YELLOW);}
                case 4 -> {g.setColor(Color.GREEN);}
                case 5 -> {g.setColor(Color.BLUE);}
                case 6 -> {g.setColor(Color.magenta);}
            }
            
            if (is_barrier_hitting_mouse(barr))
            {
                switch (CURRENT_MODE)
                {
                    
                    case 4 -> {
                        if (MOVE_BUFFER == null)
                        {
                            MOVE_BUFFER = barr;
                        }
                    }
                    
                    case 2, 6, 7 -> {
                        if (MOUSE_IS_HELD && MOUSE_PRESSED_TYPE == 3) {
                            MAP_BARRIERS.remove(i);
                        }
                    }
                    
                    case 3 -> {
                        if (MOUSE_IS_HELD)
                        {
                            addundostate();
                            MAP_BARRIERS.remove(i);
                            addundopost();
                        }
                        g.setColor(Color.LIGHT_GRAY);
                    }
                }
            }
            
            if (MOVE_BUFFER == barr)
            {
                g.setColor(Color.LIGHT_GRAY);
            }
            
            if (is_barrier_hitting_selection(barr) || SELECTIONS_BUFFER.contains(barr))
            {
                g.setColor(Color.LIGHT_GRAY);
            }

            g.fillRect(barr.x, barr.y, barr.width, barr.height);
        }
        
        for (int i = 0; i < MAP_ENTITIES.size(); i++)
        {
            BT_Entity enti = MAP_ENTITIES.get(i);
            
            int entiwidth = Entity_Images[enti.id][enti.type].image.getWidth();
            int entiheight = Entity_Images[enti.id][enti.type].image.getHeight();
            int entioriginx = Entity_Images[enti.id][enti.type].x;
            int entioriginy = Entity_Images[enti.id][enti.type].y;
            
            if (MOVE_BUFFER == enti)
            {
                g.setColor(Color.WHITE);
                g.fillRect(enti.x - entioriginx, enti.y - entioriginy, entiwidth, entiheight);
            }
            
            if (is_entity_hitting_selection(enti) || SELECTIONS_BUFFER.contains(enti))
            {
                g.setColor(Color.WHITE);
                g.fillRect(enti.x - entioriginx, enti.y - entioriginy, entiwidth, entiheight);
            }
            
            if ((enti.x + entiwidth - entioriginx > mousePosition.x) && (enti.x - entioriginx < mousePosition.x) && (enti.y + entiheight - entioriginy > mousePosition.y) && (enti.y - entioriginy < mousePosition.y))
            {
                switch (CURRENT_MODE)
                {
                    case 4 ->
                    {
                        if (MOVE_BUFFER == null)
                        {
                            MOVE_BUFFER = enti;
                            g.setColor(Color.WHITE);
                            g.fillRect(enti.x - entioriginx, enti.y - entioriginy, entiwidth, entiheight);
                        }
                        
                    }
                    
                    case 3 -> {
                        if (MOUSE_IS_HELD)
                        {
                            MAP_ENTITIES.remove(i);
                        }
                        g.setColor(Color.WHITE);
                        g.fillRect(enti.x - entioriginx, enti.y - entioriginy, entiwidth, entiheight);
                    }
                }    

            }
            BT_Sprite img = Entity_Images[enti.id][enti.type];
            g.drawImage(img.image, enti.x - img.x, enti.y - img.y, this);
            
        }
        
    }
    
    private void clear_screen()
    {
        g.setColor(Color.BLACK);
        g.fillRect(MAP_X * 480, MAP_Y * 360, 480, 360);
        
        if (GRID_TOGGLE)
        {
            g.setColor(new Color(80, 80, 80));
            for (int x = 0; x < 60; x++)
            {
                switch (x)
                {
                    default -> g.drawLine(x * 8, 0, x * 8, 360);
                    
                    case 30 -> {
                        g.setColor(new Color(120, 120, 120));
                        g.drawLine(x * 8, 0, x * 8, 360);
                        g.setColor(new Color(80, 80, 80));
                    }
                }
                
            }
            
            for (int y = 0; y < 45; y++)
            {
                switch (y)
                {
                    default -> g.drawLine(0, y * 8, 480, y * 8);
                    
                    case 22 -> {
                        g.setColor(new Color(120, 120, 120));
                        g.drawLine(0, y * 8, 480, y * 8);
                        g.setColor(new Color(80, 80, 80));
                    }
                }
                
            }
        }
        
        draw_viewer();
        Point thepoint = map_view_panel.getMousePosition();
        
        BufferedImage bi2 = new BufferedImage(480, 360, BufferedImage.TYPE_INT_RGB);
        bi2 = bi.getSubimage(MAP_X * 480, MAP_Y * 360, 480, 360);
        
        if (thepoint == null)
        {
            mvg.drawImage(bi2, 0, 0, this);
            return;
        }
        mousePosition = thepoint;
        mousePosition.x = mousePosition.x + (MAP_X * 480);
        mousePosition.y = mousePosition.y + (MAP_Y * 360);
        
        g.setColor(COLORLIST[CURRENT_COLOR]);
        switch (CURRENT_MODE)
        {
            // SELECT TOOL
            case 0 ->
            {
                if (SELECT_BUFFER == null && MOUSE_IS_HELD)
                {
                    SELECTIONS_BUFFER.clear();
                    SELECT_BUFFER = new SELECT_TOOL_BUFFER(mousePosition.x, mousePosition.y);
                }
                
                if (SELECT_BUFFER != null)
                {
                    SELECT_BUFFER.width = mousePosition.x - SELECT_BUFFER.startx;
                    SELECT_BUFFER.height = mousePosition.y - SELECT_BUFFER.starty;
                    g.setColor(Color.WHITE);
                    g.drawRect(SELECT_BUFFER.startx, SELECT_BUFFER.starty, SELECT_BUFFER.width, SELECT_BUFFER.height);   
                }
                
                if (!MOUSE_IS_HELD && SELECT_BUFFER != null)
                {
                    for (int i = 0; i < MAP_BARRIERS.size(); i++)
                    {
                        BT_Barrier barr = MAP_BARRIERS.get(i);
                        if (is_barrier_hitting_selection(barr))
                        {
                            SELECTIONS_BUFFER.add(barr);
                            System.out.println("SELECTED " + barr);
                        }
                    }
                    
                    for (int i = 0; i < MAP_ENTITIES.size(); i++)
                    {
                        BT_Entity enti = MAP_ENTITIES.get(i);
                        if (is_entity_hitting_selection(enti))
                        {
                            SELECTIONS_BUFFER.add(enti);
                            System.out.println("SELECTED " + enti);
                        }
                    }
                    
                    SELECT_BUFFER = null;
                }
                
            }
            
            // WALL
            case 2 -> {
                g.drawRect((mousePosition.x/8) * 8, (mousePosition.y/8) * 8, 8, 8);
                boolean canplace = canbeplaced();
                if (canplace && MOUSE_IS_HELD && MOUSE_PRESSED_TYPE != 3)
                {
                    addundostate();
                    MAP_BARRIERS.add(new BT_Barrier((mousePosition.x/8)*8, (mousePosition.y/8)*8, 8, 8, CURRENT_COLOR));
                    addundopost();
                    
                }
            }
            
            // STAMP
            case 5 -> {
                BT_Sprite img = Entity_Images[CURRENT_ENTITY][CURRENT_ENTITY_TYPE];
                g.drawImage(img.image, ((mousePosition.x/8) * 8) - img.x, ((mousePosition.y/8) * 8) - img.y, this);
            }
            
            // VERTICAL WALL
            case 6 -> {
                g.drawRect((mousePosition.x/8) * 8, (mousePosition.y/8) * 8, 8, 8);
                if (vert_wall_buffer != null)
                {
                    vert_wall_buffer.width = 8;
                    vert_wall_buffer.height = (int) (java.lang.Math.floor((mousePosition.y - vert_wall_buffer.y)/8)*8) + 8;
                    g.setColor(COLORLIST[CURRENT_COLOR]);
                    g.fillRect(vert_wall_buffer.x, vert_wall_buffer.y, 8, vert_wall_buffer.height);
                }
            }
            
            // HORIZONTAL WALL 
            case 7 -> {
                g.drawRect((mousePosition.x/8) * 8, (mousePosition.y/8) * 8, 8, 8);
                if (hori_wall_buffer != null)
                {
                    hori_wall_buffer.width = (int) (java.lang.Math.floor((mousePosition.x - hori_wall_buffer.x)/8)*8) + 8;
                    hori_wall_buffer.height = 8;
                    g.setColor(COLORLIST[CURRENT_COLOR]);
                    g.fillRect(hori_wall_buffer.x, hori_wall_buffer.y, hori_wall_buffer.width, 8);
                }
            }
            
        }
       
        mvg.drawImage(bi2, 0, 0, this);
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BTME2UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BTME2UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BTME2UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BTME2UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BTME2UI().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btn_delete;
    private javax.swing.JToggleButton btn_horiline;
    private javax.swing.JToggleButton btn_move;
    private javax.swing.JButton btn_name;
    private javax.swing.JButton btn_objects;
    private javax.swing.JToggleButton btn_pencil;
    private javax.swing.JToggleButton btn_select;
    public static javax.swing.JToggleButton btn_stamp;
    private javax.swing.JToggleButton btn_vertline;
    public static javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton down;
    private javax.swing.JMenu edit;
    private javax.swing.JMenuItem exportmap;
    private javax.swing.JMenu file;
    private javax.swing.JMenuItem importmap;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private static javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton left;
    private javax.swing.JPanel map_view_panel;
    private javax.swing.JMenuBar menubar;
    private javax.swing.JButton right;
    private javax.swing.JMenuItem undo;
    private javax.swing.JButton up;
    // End of variables declaration//GEN-END:variables
}
