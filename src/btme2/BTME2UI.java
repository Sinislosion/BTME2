/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
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
import static javax.swing.BorderFactory.createBevelBorder;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;

/**
 *
 * @author barackobama
 */

public class BTME2UI extends javax.swing.JFrame {
    
    /**
     *  The name given to the map
     */
    public static String MAP_NAME = "PLACE";
    
    public static LinkedList<BT_Barrier> MAP_BARRIERS = new LinkedList<>();
    
    public int CURRENT_MODE = 2;
    public int CURRENT_COLOR = 5;
    public int IS_ON_VIEWER = 0;
    
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
    BufferedImage btronimg;
    
    Cursor amicursor;
    Image cursorimg;
    
    JFrame choose;
    JFrame namechoose;
    
    Point mousePosition;
    
    static int labelopacity = 255;
    static int labeltime = 512;
    
    private static BT_Barrier vert_wall_buffer;
    private static BT_Barrier hori_wall_buffer;
    private static BT_Barrier MOVE_BUFFER = null;
    private static BT_Barrier DEL_BUFFER = null;
    public boolean MOUSE_IS_HELD = false;
    public boolean GRID_TOGGLE = false;
    
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
        
        // netbeans sstuff
        initComponents();
        
        // i forgot what this does
        bi = new BufferedImage(480, 360, BufferedImage.TYPE_INT_RGB);
        g = bi.createGraphics();
        mvg = map_view_panel.getGraphics();
        
        
        // set the window icon
        Image iconimg;
        iconimg = new ImageIcon(this.getClass().getResource("/icon.png")).getImage();
        try {
            File shit = new File(this.getClass().getResource("btron_b3.png").getPath());
            System.out.println(shit.getCanonicalPath());
            btronimg = ImageIO.read(shit);
        } catch (IOException ex) {
            Logger.getLogger(BTME2UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // set the cursor
        cursorimg = new ImageIcon(this.getClass().getResource("/cursor.png")).getImage();
        amicursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorimg, new Point(0,0), "Amiga");
        setCursor(amicursor);
        
        // set popup windows
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
    
    public void changealert(String message)
    {
        BTME2UI.labelopacity = 255;
        BTME2UI.labeltime = 512;
        jLabel3.setText(message);
        jLabel3.setForeground(new Color (255, 255, 255, labelopacity));
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
        jMenuItem3 = new javax.swing.JMenuItem();
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
        menubar = new javax.swing.JMenuBar();
        file = new javax.swing.JMenu();
        importmap = new javax.swing.JMenuItem();
        exportmap = new javax.swing.JMenuItem();
        edit = new javax.swing.JMenu();
        undo = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        jMenuItem3.setText("jMenuItem3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("B-TRON MAP EDITOR 2");
        setAutoRequestFocus(false);
        setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        setForeground(java.awt.Color.white);
        setName("B-TRON MAP EDITOR 2"); // NOI18N
        setPreferredSize(new java.awt.Dimension(720, 540));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setCursor(amicursor);
        jPanel1.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
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

        buttonGroup2.add(btn_select);
        btn_select.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/select.png"))); // NOI18N
        btn_select.setMaximumSize(new java.awt.Dimension(32, 32));
        btn_select.setMinimumSize(new java.awt.Dimension(32, 32));
        btn_select.setPreferredSize(new java.awt.Dimension(32, 32));
        btn_select.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_selectMouseClicked(evt);
            }
        });
        jPanel2.add(btn_select);

        btn_objects.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/objects.png"))); // NOI18N
        buttonGroup2.add(btn_objects);
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

        buttonGroup2.add(btn_pencil);
        btn_pencil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/penis.png"))); // NOI18N
        btn_pencil.setSelected(true);
        btn_pencil.setMaximumSize(new java.awt.Dimension(32, 32));
        btn_pencil.setMinimumSize(new java.awt.Dimension(32, 32));
        btn_pencil.setPreferredSize(new java.awt.Dimension(32, 32));
        btn_pencil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pencilActionPerformed(evt);
            }
        });
        jPanel2.add(btn_pencil);

        buttonGroup2.add(btn_delete);
        btn_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/delete.png"))); // NOI18N
        btn_delete.setMaximumSize(new java.awt.Dimension(32, 32));
        btn_delete.setMinimumSize(new java.awt.Dimension(32, 32));
        btn_delete.setPreferredSize(new java.awt.Dimension(32, 32));
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });
        jPanel2.add(btn_delete);

        buttonGroup2.add(btn_move);
        btn_move.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/move.png"))); // NOI18N
        btn_move.setMaximumSize(new java.awt.Dimension(32, 32));
        btn_move.setMinimumSize(new java.awt.Dimension(32, 32));
        btn_move.setPreferredSize(new java.awt.Dimension(32, 32));
        btn_move.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_moveActionPerformed(evt);
            }
        });
        jPanel2.add(btn_move);

        buttonGroup2.add(btn_stamp);
        btn_stamp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/stamp.png"))); // NOI18N
        btn_stamp.setMaximumSize(new java.awt.Dimension(32, 32));
        btn_stamp.setMinimumSize(new java.awt.Dimension(32, 32));
        btn_stamp.setPreferredSize(new java.awt.Dimension(32, 32));
        btn_stamp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_stampActionPerformed(evt);
            }
        });
        jPanel2.add(btn_stamp);

        buttonGroup2.add(btn_vertline);
        btn_vertline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/vert-line.png"))); // NOI18N
        btn_vertline.setMaximumSize(new java.awt.Dimension(32, 32));
        btn_vertline.setMinimumSize(new java.awt.Dimension(32, 32));
        btn_vertline.setPreferredSize(new java.awt.Dimension(32, 32));
        btn_vertline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_vertlineActionPerformed(evt);
            }
        });
        jPanel2.add(btn_vertline);

        buttonGroup2.add(btn_horiline);
        btn_horiline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/hori-line.png"))); // NOI18N
        btn_horiline.setMaximumSize(new java.awt.Dimension(32, 32));
        btn_horiline.setMinimumSize(new java.awt.Dimension(32, 32));
        btn_horiline.setPreferredSize(new java.awt.Dimension(32, 32));
        btn_horiline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_horilineActionPerformed(evt);
            }
        });
        jPanel2.add(btn_horiline);

        btn_name.setIcon(new javax.swing.ImageIcon(getClass().getResource("/btme2/text.png"))); // NOI18N
        buttonGroup2.add(btn_name);
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

        jLabel1.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Border Color");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 450, 110, -1));

        jComboBox1.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Black", "Red", "Orange", "Yellow", "Green", "Blue", "Violet" }));
        jComboBox1.setSelectedIndex(5);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 470, 110, -1));

        jLabel2.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Viewer");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, -1, -1));

        jLabel3.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Initialized");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 480, -1, -1));

        menubar.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Propo", 0, 12)); // NOI18N
        menubar.setName("Menu"); // NOI18N

        file.setText("File");
        file.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N

        importmap.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        importmap.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        importmap.setText("Import Map");
        file.add(importmap);

        exportmap.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        exportmap.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        exportmap.setText("Export Map");
        exportmap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportmapActionPerformed(evt);
            }
        });
        file.add(exportmap);

        menubar.add(file);

        edit.setText("Edit");
        edit.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N

        undo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        undo.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        undo.setLabel("Undo");
        undo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoActionPerformed(evt);
            }
        });
        edit.add(undo);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem1.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
        jMenuItem1.setText("Redo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        edit.add(jMenuItem1);

        menubar.add(edit);

        jMenu1.setText("View");
        jMenu1.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setFont(new java.awt.Font("BigBlueTerm437 Nerd Font Mono", 0, 12)); // NOI18N
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, 514, 514, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void undoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_undoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseEntered

    }//GEN-LAST:event_jPanel1MouseEntered

    public boolean canbeplaced()
    {
        Point mousepoint = map_view_panel.getMousePosition();
        boolean canplace = true;
        for (int i = 0; i < MAP_BARRIERS.size(); i++)
        {
            BT_Barrier barr = MAP_BARRIERS.get(i);
            canplace = (barr.x != (mousepoint.x/8)*8 || barr.y != (mousepoint.y/8)*8);
            if (canplace == false) {return false;}

        }
        return true;
    }
    
    private void map_view_panelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_map_view_panelMousePressed

        switch (CURRENT_MODE)
        {
            case 2 -> {
                Point mousepoint = map_view_panel.getMousePosition();
                boolean canplace = canbeplaced();
                if (canplace == true)
                {
                    MAP_BARRIERS.add(new BT_Barrier((mousepoint.x/8)*8, (mousepoint.y/8)*8, 8, 8, CURRENT_COLOR));
                    System.out.println("WALL ADDED");
                }
                else
                {
                    System.out.println("WALL ALREADY EXISTS HERE");
                }
            }
            
            case 6 ->
            {
                Point mousepoint = map_view_panel.getMousePosition();
                boolean canplace = canbeplaced();
                if (canplace == true)
                {
                    if (vert_wall_buffer == null)
                    {
                        vert_wall_buffer = new BT_Barrier((mousepoint.x/8)*8, (mousepoint.y/8)*8, 8, 8, CURRENT_COLOR);
                    }
                }
                else
                {
                    System.out.println("WALL ALREADY EXISTS HERE");
                }
            }
            
            case 7 ->
            {
                Point mousepoint = map_view_panel.getMousePosition();
                boolean canplace = canbeplaced();
                if (canplace == true)
                {
                    if (hori_wall_buffer == null)
                    {
                        hori_wall_buffer = new BT_Barrier((mousepoint.x/8)*8, (mousepoint.y/8)*8, 8, 8, CURRENT_COLOR);
                    }
                }
                else
                {
                    System.out.println("WALL ALREADY EXISTS HERE");
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
        
    }//GEN-LAST:event_map_view_panelMouseEntered

    private void map_view_panelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_map_view_panelMouseMoved
        // TODO add your handling code here:

    }//GEN-LAST:event_map_view_panelMouseMoved

    private void btn_pencilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pencilActionPerformed
        System.out.println("PENCIL TOOL SELECTED");        // TODO add your handling code here:
        changealert("Pencil Tool Selected!");
        CURRENT_MODE = 2;
    }//GEN-LAST:event_btn_pencilActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        System.out.println("DELETE TOOL SELECTED"); // TODO add your handling code here:
        changealert("Delete Tool Selected!");
        CURRENT_MODE = 3;
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_selectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_selectMouseClicked
        System.out.println("SELECT TOOL SELECTED");
        changealert("Select Tool Selected!");
        CURRENT_MODE = 0;        // TODO add your handling code here:
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
        CURRENT_MODE = 4;        // TODO add your handling code here:
    }//GEN-LAST:event_btn_moveActionPerformed

    private void btn_stampActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_stampActionPerformed
        System.out.println("STAMP TOOL SELECTED");
        changealert("Stamp Tool Selected!");
        CURRENT_MODE = 5;        // TODO add your handling code here:
    }//GEN-LAST:event_btn_stampActionPerformed

    private void btn_vertlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vertlineActionPerformed
        System.out.println("VERTICAL LINE TOOL SELECTED");
        changealert("Vertical Line Tool Selected!");
        CURRENT_MODE = 6;        // TODO add your handling code here:
    }//GEN-LAST:event_btn_vertlineActionPerformed

    private void btn_horilineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_horilineActionPerformed
        System.out.println("HORIZONTAL LINE TOOL SELECTED");
        CURRENT_MODE = 7;        // TODO add your handling code here:
    }//GEN-LAST:event_btn_horilineActionPerformed

    private void map_view_panelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_map_view_panelMouseReleased
        switch (CURRENT_MODE)
        {
            case 6 ->
            {
                if (vert_wall_buffer != null && vert_wall_buffer.height > 0)
                {
                    MAP_BARRIERS.add(vert_wall_buffer);
                    vert_wall_buffer = null;
                    System.out.println("WALL ADDED");
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
                    MAP_BARRIERS.add(hori_wall_buffer);
                    hori_wall_buffer = null;
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
            FileOutputStream savedmap = new FileOutputStream("export.btm");
            byte HEADER[] = {'B', 'T', 'M', 'v', 2, 0, 0, 0};
            savedmap.write(HEADER);
            
            // WRITE THE NAME OF THE MAP
            for (int i = 0; i < 16; i++)
            {
                if (i < MAP_NAME.length()){
                    savedmap.write(MAP_NAME.charAt(i));
                }
                else
                {
                    savedmap.write('\n');
                    break;
                }
            }
            
            for (int i = 0; i < MAP_BARRIERS.size(); i++)
            {
                savedmap.write(((MAP_BARRIERS.get(i).x) / 8) >> 8);
                savedmap.write((MAP_BARRIERS.get(i).x) / 8);
                
                savedmap.write(((MAP_BARRIERS.get(i).y) / 8) >> 8);
                savedmap.write((MAP_BARRIERS.get(i).y) / 8);
                
                savedmap.write(((MAP_BARRIERS.get(i).width) / 8) >> 8);
                savedmap.write((MAP_BARRIERS.get(i).width) / 8);
                
                savedmap.write(((MAP_BARRIERS.get(i).height) / 8) >> 8);
                savedmap.write((MAP_BARRIERS.get(i).height) / 8);
                
                savedmap.write(MAP_BARRIERS.get(i).color);
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

    private void draw_barriers()
    {
        if (MOUSE_IS_HELD && MOVE_BUFFER != null)
        {
            MOVE_BUFFER.x = (mousePosition.x / 8) * 8;
            MOVE_BUFFER.y = (mousePosition.y / 8) * 8;
        }
        
        if (!MOUSE_IS_HELD)
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
            
            if ((barr.x + barr.width > mousePosition.x) && (barr.x < mousePosition.x) && (barr.y + barr.height > mousePosition.y) && (barr.y < mousePosition.y))
            {
                switch (CURRENT_MODE)
                {
                    default ->
                    {
                        MOVE_BUFFER = null;
                    }
                    
                    case 4 -> {
                        if (MOVE_BUFFER == null)
                        {
                            MOVE_BUFFER = barr;
                        }
                    }
                    
                    case 3 -> {
                        if (MOUSE_IS_HELD)
                        {
                            MAP_BARRIERS.remove(i);
                        }
                        g.setColor(Color.LIGHT_GRAY);
                    }
                }
            }
            
            if (MOVE_BUFFER == barr) 
            {
                g.setColor(Color.LIGHT_GRAY);
            }

            g.fillRect(barr.x, barr.y, barr.width, barr.height);
        }
    }
    
    private void clear_screen()
    {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 480, 360);
                
        g.drawImage(btronimg, 0, 0, this);
        
        if (GRID_TOGGLE)
        {
            g.setColor(new Color(80, 80, 80));
            for (int x = 0; x < 60; x++)
            {
                g.drawLine(x * 8, 0, x * 8, 360);
            }
            
            for (int y = 0; y < 45; y++)
            {
                g.drawLine(0, y * 8, 480, y * 8);
            }
        }
        
        draw_barriers();
        Point thepoint = map_view_panel.getMousePosition();
        if (thepoint == null)
        {
            mvg.drawImage(bi, 0, 0, this);
            return;
        }
        mousePosition = thepoint;
        
        g.setColor(COLORLIST[CURRENT_COLOR]);
        switch (CURRENT_MODE)
        {
            case 2 -> {g.drawRect((mousePosition.x/8) * 8, (mousePosition.y/8) * 8, 8, 8);}
            case 6 -> {g.drawRect((mousePosition.x/8) * 8, (mousePosition.y/8) * 8, 8, 8);}
            case 7 -> {g.drawRect((mousePosition.x/8) * 8, (mousePosition.y/8) * 8, 8, 8);}
        }
        
        switch (CURRENT_MODE)
        {
            
            case 6 ->
            {
                if (vert_wall_buffer != null)
                {
                    vert_wall_buffer.width = 8; //(int) (java.lang.Math.ceil((mousepoint.x - vert_wall_buffer.x)/8)*8);
                    vert_wall_buffer.height = (int) (java.lang.Math.floor((mousePosition.y - vert_wall_buffer.y)/8)*8) + 8;
                    g.setColor(COLORLIST[CURRENT_COLOR]);
                    g.fillRect(vert_wall_buffer.x, vert_wall_buffer.y, 8, vert_wall_buffer.height);
                }
                
            }
            
            case 7 ->
            {
                if (hori_wall_buffer != null)
                {
                    hori_wall_buffer.width = (int) (java.lang.Math.floor((mousePosition.x - hori_wall_buffer.x)/8)*8) + 8;
                    hori_wall_buffer.height = 8;
                    g.setColor(COLORLIST[CURRENT_COLOR]);
                    g.fillRect(hori_wall_buffer.x, hori_wall_buffer.y, hori_wall_buffer.width, 8);
                }
                
            }

        }
        
        mvg.drawImage(bi, 0, 0, this);
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
    private javax.swing.JToggleButton btn_stamp;
    private javax.swing.JToggleButton btn_vertline;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JMenu edit;
    private javax.swing.JMenuItem exportmap;
    private javax.swing.JMenu file;
    private javax.swing.JMenuItem importmap;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel map_view_panel;
    private javax.swing.JMenuBar menubar;
    private javax.swing.JMenuItem undo;
    // End of variables declaration//GEN-END:variables
}
