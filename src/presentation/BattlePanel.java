package presentation;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import presentation.dto.*;

public class BattlePanel extends PoobkemonPanel {
    private EntrenadorDTO entrenador1, entrenador2;
    private PokemonDTO pokemon1, pokemon2;
    private boolean turnoEntrenador1;
    private Random random;
    
    private ImagePanel backgroundPanel;
    private JLabel pokemon1ImageLabel, pokemon2ImageLabel;
    private JProgressBar hpBar1, hpBar2;
    private JLabel nameLabel1, nameLabel2;
    private JLabel levelLabel1, levelLabel2;
    private JLabel hpLabel1, hpLabel2;
    private JPanel messagePanel;
    private JTextArea messageArea;
    private JPanel buttonsPanel;
    private JButton attackButton, bagButton, pokemonButton, runButton, pauseButton;

    private final Color HP_BAR_COLOR = new Color(112, 248, 168);
    private final Color MESSAGE_PANEL_COLOR = new Color(69, 129, 142);
    private final Color MESSAGE_BORDER_COLOR = new Color(235, 76, 66);
    private final Color BUTTON_COLOR = new Color(192, 192, 192);
    private final Color BUTTON_BORDER_COLOR = Color.DARK_GRAY;
    private final Font PIXEL_FONT = new Font("SansSerif", Font.BOLD, 14);

    public BattlePanel(PoobkemonGUI mainGUI) {
        super(mainGUI);
        random = new Random();

        entrenador1 = mainGUI.getEntrenador1DTO();
        entrenador2 = mainGUI.getEntrenador2DTO();
        
        if (entrenador1 == null || entrenador2 == null) {
            JOptionPane.showMessageDialog(null, 
                "Error al iniciar la batalla: No se pudieron cargar los entrenadores", 
                "Error", JOptionPane.ERROR_MESSAGE);
            mainGUI.showMainMenu();
            return;
        }

        pokemon1 = obtenerPrimerPokemonDisponible(entrenador1);
        pokemon2 = obtenerPrimerPokemonDisponible(entrenador2);
        
        if (pokemon1 == null || pokemon2 == null) {
            JOptionPane.showMessageDialog(null, 
                "Error al iniciar la batalla: No hay Pokémon disponibles", 
                "Error", JOptionPane.ERROR_MESSAGE);
            mainGUI.showMainMenu();
            return;
        }

        turnoEntrenador1 = true;
        
        initialize();
        actualizarInterfaz();
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarComponentes();
            }
            
            @Override
            public void componentShown(ComponentEvent e) {
                ajustarComponentes();
            }
        });
        
        SwingUtilities.invokeLater(() -> {
            EntrenadorDTO entrenadorActual = turnoEntrenador1 ? entrenador1 : entrenador2;
            PokemonDTO pokemonActual = turnoEntrenador1 ? pokemon1 : pokemon2;
            
            if (esCPU(entrenadorActual)) {
                Timer timer = new Timer(1500, e -> jugarTurnoCPU());
                timer.setRepeats(false);
                timer.start();
            } else {
                messageArea.setText("¿Qué debería hacer " + pokemonActual.getName() + "?");
            }
        });
    }

    @Override
    protected void prepareElements() {
        backgroundPanel = new ImagePanel("images/background/battle_background.png");
        backgroundPanel.setLayout(null);

        setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);
        
        nameLabel2 = new JLabel(pokemon2.getName());
        nameLabel2.setFont(PIXEL_FONT);
        backgroundPanel.add(nameLabel2);
        
        levelLabel2 = new JLabel("Nv. 100");
        levelLabel2.setFont(PIXEL_FONT);
        backgroundPanel.add(levelLabel2);
        
        hpBar2 = new JProgressBar(0, pokemon2.getMaxPs());
        hpBar2.setValue(pokemon2.getPs());
        hpBar2.setForeground(HP_BAR_COLOR);
        hpBar2.setBackground(Color.BLACK);
        hpBar2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        backgroundPanel.add(hpBar2);
        
        hpLabel2 = new JLabel(pokemon2.getPs() + "/" + pokemon2.getMaxPs());
        hpLabel2.setFont(PIXEL_FONT);
        hpLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
        backgroundPanel.add(hpLabel2);

        nameLabel1 = new JLabel(pokemon1.getName());
        nameLabel1.setFont(PIXEL_FONT);
        backgroundPanel.add(nameLabel1);
        
        levelLabel1 = new JLabel("Nv. 100");
        levelLabel1.setFont(PIXEL_FONT);
        backgroundPanel.add(levelLabel1);
        
        hpBar1 = new JProgressBar(0, pokemon1.getMaxPs());
        hpBar1.setValue(pokemon1.getPs());
        hpBar1.setForeground(HP_BAR_COLOR);
        hpBar1.setBackground(Color.BLACK);
        hpBar1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        backgroundPanel.add(hpBar1);
        
        hpLabel1 = new JLabel(pokemon1.getPs() + "/" + pokemon1.getMaxPs());
        hpLabel1.setFont(PIXEL_FONT);
        hpLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
        backgroundPanel.add(hpLabel1);

        pauseButton = new JButton("Pausa");
        pauseButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        pauseButton.setBackground(new Color(70, 130, 180));
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setFocusPainted(false);
        pauseButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        backgroundPanel.add(pauseButton);
        
        pokemon1ImageLabel = new JLabel();
        ImageIcon icon1 = loadPokemonImageBack(pokemon1.getName().toLowerCase());
        if (icon1 != null) {
            pokemon1ImageLabel.setIcon(icon1);
        }
        backgroundPanel.add(pokemon1ImageLabel);

        pokemon2ImageLabel = new JLabel();
        ImageIcon icon2 = loadPokemonImage(pokemon2.getName().toLowerCase());
        if (icon2 != null) {
            pokemon2ImageLabel.setIcon(icon2);
        }
        backgroundPanel.add(pokemon2ImageLabel);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.setBackground(MESSAGE_PANEL_COLOR);
        messagePanel.setBorder(BorderFactory.createLineBorder(MESSAGE_BORDER_COLOR, 5, true));
        
        PokemonDTO pokemonActual = turnoEntrenador1 ? pokemon1 : pokemon2;
        messageArea = new JTextArea("¿Qué debería hacer " + pokemonActual.getName() + "?");
        messageArea.setEditable(false);
        messageArea.setFont(new Font("SansSerif", Font.BOLD, 23));
        messageArea.setForeground(Color.BLACK);
        messageArea.setBackground(MESSAGE_PANEL_COLOR);
        messageArea.setBorder(new EmptyBorder(10, 20, 10, 20));
        messagePanel.add(messageArea, BorderLayout.CENTER);
        
        backgroundPanel.add(messagePanel);

        buttonsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buttonsPanel.setBackground(new Color(0, 0, 0, 0));
        
        attackButton = createButton("ATACAR");
        bagButton = createButton("MOCHILA");
        pokemonButton = createButton("POKÉMON");
        runButton = createButton("HUIR");
        
        buttonsPanel.add(attackButton);
        buttonsPanel.add(bagButton);
        buttonsPanel.add(pokemonButton);
        buttonsPanel.add(runButton);
        
        backgroundPanel.add(buttonsPanel);

        ajustarComponentes();
    }

    /**
     * Ajusta los componentes basado en el tamaño actual de la ventana
     */
    private void ajustarComponentes() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        if (panelWidth <= 0 || panelHeight <= 0) return;
        
        try {
            if (backgroundPanel != null) {
                backgroundPanel.setBounds(0, 0, panelWidth, panelHeight);

                if (nameLabel1.getParent() == null) backgroundPanel.add(nameLabel1);
                if (levelLabel1.getParent() == null) backgroundPanel.add(levelLabel1);
                if (hpBar1.getParent() == null) backgroundPanel.add(hpBar1);
                if (hpLabel1.getParent() == null) backgroundPanel.add(hpLabel1);
                
                if (nameLabel2.getParent() == null) backgroundPanel.add(nameLabel2);
                if (levelLabel2.getParent() == null) backgroundPanel.add(levelLabel2);
                if (hpBar2.getParent() == null) backgroundPanel.add(hpBar2);
                if (hpLabel2.getParent() == null) backgroundPanel.add(hpLabel2);

                JPanel statsFrame2 = null;
                JPanel statsFrame1 = null;
                
                for (Component c : backgroundPanel.getComponents()) {
                    if (c instanceof JPanel && !(c.equals(messagePanel) || c.equals(buttonsPanel))) {
                        if (c.getY() < panelHeight/2) {
                            statsFrame2 = (JPanel)c;
                        } else {
                            statsFrame1 = (JPanel)c;
                        }
                    }
                }

                int enemyX = (int)(panelWidth * 0.07);
                int enemyY = (int)(panelHeight * 0.13);
                int enemyWidth = (int)(panelWidth * 0.35);
                int statsHeight = 80;

                int playerX = (int)(panelWidth * 0.58);
                int playerY = (int)(panelHeight * 0.50);
                int playerWidth = (int)(panelWidth * 0.35);

                nameLabel2.setBounds(enemyX + 15, enemyY + 10, enemyWidth / 2 - 30, 25);
                levelLabel2.setBounds(enemyX + enemyWidth - 90, enemyY + 10, 80, 25);
                hpBar2.setBounds(enemyX + 25, enemyY + 45, enemyWidth - 95, 15);
                hpLabel2.setBounds(enemyX + enemyWidth - 65, enemyY + 45, 60, 15);

                nameLabel1.setBounds(playerX + 15, playerY + 10, playerWidth / 2 - 30, 25);
                levelLabel1.setBounds(playerX + playerWidth - 90, playerY + 10, 80, 25);
                hpBar1.setBounds(playerX + 25, playerY + 45, playerWidth - 95, 15);
                hpLabel1.setBounds(playerX + playerWidth - 65, playerY + 45, 60, 15);

                if (pauseButton != null) {
                    int buttonWidth = 80;
                    int buttonHeight = 30;
                    int margin = 15;
                    
                    pauseButton.setBounds(panelWidth - buttonWidth - margin, margin, buttonWidth, buttonHeight);
                    backgroundPanel.setComponentZOrder(pauseButton, 0);
                }

                if (pokemon1ImageLabel != null) {
                    if (pokemon1ImageLabel.getParent() == null) {
                        backgroundPanel.add(pokemon1ImageLabel);
                    }

                    int x1 = (int)(panelWidth * 0.20);
                    int y1 = (int)(panelHeight * 0.50);

                    int pokemonWidth = 150;
                    int pokemonHeight = 150;
                    if (pokemon1ImageLabel.getIcon() != null) {
                        pokemonWidth = pokemon1ImageLabel.getIcon().getIconWidth();
                        pokemonHeight = pokemon1ImageLabel.getIcon().getIconHeight();
                    }

                    int maxSize = (int)(Math.min(panelWidth, panelHeight) * 0.20);
                    double scale = Math.min((double)maxSize / pokemonWidth, (double)maxSize / pokemonHeight);
                    int scaledWidth = (int)(pokemonWidth * scale);
                    int scaledHeight = (int)(pokemonHeight * scale);

                    if (pokemon1ImageLabel.getIcon() != null && (scaledWidth != pokemonWidth || scaledHeight != pokemonHeight)) {
                        Image img = ((ImageIcon)pokemon1ImageLabel.getIcon()).getImage();
                        Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                        pokemon1ImageLabel.setIcon(new ImageIcon(scaledImg));
                    }
                    
                    pokemon1ImageLabel.setBounds(x1, y1, scaledWidth, scaledHeight);
                    backgroundPanel.setComponentZOrder(pokemon1ImageLabel, 0);
                }

                if (pokemon2ImageLabel != null) {
                    if (pokemon2ImageLabel.getParent() == null) {
                        backgroundPanel.add(pokemon2ImageLabel);
                    }

                    int x2 = (int)(panelWidth * 0.68);
                    int y2 = (int)(panelHeight * 0.20);

                    int pokemonWidth = 150;
                    int pokemonHeight = 150;
                    if (pokemon2ImageLabel.getIcon() != null) {
                        pokemonWidth = pokemon2ImageLabel.getIcon().getIconWidth();
                        pokemonHeight = pokemon2ImageLabel.getIcon().getIconHeight();
                    }

                    int maxSize = (int)(Math.min(panelWidth, panelHeight) * 0.20);
                    double scale = Math.min((double)maxSize / pokemonWidth, (double)maxSize / pokemonHeight);
                    int scaledWidth = (int)(pokemonWidth * scale);
                    int scaledHeight = (int)(pokemonHeight * scale);

                    if (pokemon2ImageLabel.getIcon() != null && (scaledWidth != pokemonWidth || scaledHeight != pokemonHeight)) {
                        Image img = ((ImageIcon)pokemon2ImageLabel.getIcon()).getImage();
                        Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                        pokemon2ImageLabel.setIcon(new ImageIcon(scaledImg));
                    }
                    
                    pokemon2ImageLabel.setBounds(x2, y2, scaledWidth, scaledHeight);
                    backgroundPanel.setComponentZOrder(pokemon2ImageLabel, 0);
                }

                if (messagePanel != null) {
                    if (messagePanel.getParent() == null) {
                        backgroundPanel.add(messagePanel);
                    }
                    
                    int messageHeight = (int)(panelHeight * 0.20);
                    int paddingBottom = (int)(panelHeight * 0.05);
                    int paddingHorizontal = (int)(panelWidth * 0.02);
                    
                    int messageWidth = (int)(panelWidth * 0.70);
                    int messageX = paddingHorizontal;
                    int messageY = panelHeight - messageHeight - paddingBottom;
                    messagePanel.setBounds(messageX, messageY, messageWidth, messageHeight);
                    backgroundPanel.setComponentZOrder(messagePanel, 0);
                }

                if (buttonsPanel != null) {
                    if (buttonsPanel.getParent() == null) {
                        backgroundPanel.add(buttonsPanel);
                    }
                    
                    int messageHeight = (int)(panelHeight * 0.20);
                    int paddingBottom = (int)(panelHeight * 0.05);
                    int paddingHorizontal = (int)(panelWidth * 0.02);
                    
                    int messageWidth = (int)(panelWidth * 0.70);
                    int messageX = paddingHorizontal;
                    int messageY = panelHeight - messageHeight - paddingBottom;
                    
                    int buttonsWidth = (int)(panelWidth * 0.25);
                    int buttonsX = messageX + messageWidth + paddingHorizontal;
                    int buttonsY = messageY;
                    buttonsPanel.setBounds(buttonsX, buttonsY, buttonsWidth, messageHeight);
                    backgroundPanel.setComponentZOrder(buttonsPanel, 0);
                }

                for (Component c : backgroundPanel.getComponents()) {
                    if (c instanceof JLabel || c instanceof JProgressBar ||
                        c instanceof JPanel && (c == messagePanel || c == buttonsPanel)) {
                        backgroundPanel.setComponentZOrder(c, 0);
                    }
                }

                if (statsFrame2 == null) {
                    statsFrame2 = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            g.setColor(getBackground());
                            super.paintComponent(g);
                        }
                    };

                    statsFrame2.setOpaque(false);
                    statsFrame2.setBackground(new Color(0, 0, 0, 0));
                    backgroundPanel.add(statsFrame2);
                    backgroundPanel.setComponentZOrder(statsFrame2, backgroundPanel.getComponentCount() - 1);
                }
                
                if (statsFrame1 == null) {
                    statsFrame1 = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            g.setColor(getBackground());
                            super.paintComponent(g);
                        }
                    };
                    statsFrame1.setOpaque(false);
                    statsFrame1.setBackground(new Color(0, 0, 0, 0));
                    backgroundPanel.add(statsFrame1);
                    backgroundPanel.setComponentZOrder(statsFrame1, backgroundPanel.getComponentCount() - 1);
                }

                statsFrame2.setBounds(enemyX, enemyY, enemyWidth, statsHeight);
                statsFrame1.setBounds(playerX, playerY, playerWidth, statsHeight);

                backgroundPanel.setComponentZOrder(statsFrame2, backgroundPanel.getComponentCount() - 1);
                backgroundPanel.setComponentZOrder(statsFrame1, backgroundPanel.getComponentCount() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al ajustar componentes: " + e.getMessage());
        }

        revalidate();
        repaint();
    }
    
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BUTTON_BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        return button;
    }
    
    
    private ImageIcon loadPokemonImage(String pokemonName) {
        String[] possiblePaths = {
            "/images/pokemonsFront/" + pokemonName + ".png",
            "images/pokemonsFront/" + pokemonName + ".png",
            "/pokemonsFront/" + pokemonName + ".png",
            "pokemonsFront/" + pokemonName + ".png",
            "../images/pokemonsFront/" + pokemonName + ".png"
        };
        
        for (String path : possiblePaths) {
            try {
                java.net.URL imageURL = getClass().getResource(path);
                
                if (imageURL != null) {
                    return new ImageIcon(imageURL);
                }
            } catch (Exception e) {}
        }
        
        try {
            String directPath = System.getProperty("user.dir") + "/images/pokemonsFront/" + pokemonName + ".png";
            java.io.File file = new java.io.File(directPath);
            
            if (file.exists()) {
                return new ImageIcon(directPath);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar desde sistema de archivos: " + e.getMessage());
        }
        
        return null;
    }
    
    private ImageIcon loadPokemonImageBack(String pokemonName) {
        String[] possiblePaths = {
            "/images/pokemonsBack/" + pokemonName + ".png",
            "images/pokemonsBack/" + pokemonName + ".png",
            "/pokemonsBack/" + pokemonName + ".png",
            "pokemonsBack/" + pokemonName + ".png",
            "../images/pokemonsBack/" + pokemonName + ".png"
        };
        
        for (String path : possiblePaths) {
            try {
                java.net.URL imageURL = getClass().getResource(path);
                
                if (imageURL != null) {
                    return new ImageIcon(imageURL);
                }
            } catch (Exception e) {}
        }
        
        try {
            String directPath = System.getProperty("user.dir") + "/images/pokemonsBack/" + pokemonName + ".png";
            java.io.File file = new java.io.File(directPath);
            
            if (file.exists()) {
                return new ImageIcon(directPath);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar desde sistema de archivos: " + e.getMessage());
        }
        
        return null;
    }
    
    @Override
    protected void prepareActions() {
        attackButton.addActionListener(e -> mostrarAtaques());
        bagButton.addActionListener(e -> mostrarItems());
        pokemonButton.addActionListener(e -> mostrarCambioPokemon());
        runButton.addActionListener(e -> intentarHuir());
        pauseButton.addActionListener(e -> mostrarMenuPausa());
    }
    
    private void mostrarAtaques() {
        PokemonDTO pokemonActual = turnoEntrenador1 ? pokemon1 : pokemon2;
        List<MovimientoDTO> movimientos = pokemonActual.getMovimientos();
        
        if (movimientos == null || movimientos.isEmpty()) {
            messageArea.setText("¡" + pokemonActual.getName() + " no tiene movimientos disponibles!");
            return;
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(pokemonActual.getName() + " - Selecciona un movimiento:", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel movimientosPanel = new JPanel(new GridLayout(movimientos.size(), 1, 0, 5));

        final JDialog dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Movimientos", true);
        dialog.setLayout(new BorderLayout());
        
        for (MovimientoDTO movimiento : movimientos) {
            JPanel movePanel = new JPanel(new BorderLayout());
            movePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            movePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            String tipo = movimiento.getTipo() != null ? movimiento.getTipo() : "Normal";
            int poder = movimiento.getPoder();
            int precision = movimiento.getPrecision();
            int ppActual = movimiento.getPP();
            int ppMax = movimiento.getMaxPP();
            
            JPanel headerPanel = new JPanel(new BorderLayout());

            JLabel nameLabel = new JLabel(movimiento.getNombre(), JLabel.LEFT);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            nameLabel.setBorder(new EmptyBorder(5, 10, 5, 10));

            JLabel tipoLabel = new JLabel(tipo, JLabel.CENTER);
            tipoLabel.setOpaque(true);
            tipoLabel.setBackground(getTypeColor(tipo));
            tipoLabel.setForeground(Color.WHITE);
            tipoLabel.setBorder(new EmptyBorder(2, 10, 2, 10));
            tipoLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

            JPanel tipoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            tipoPanel.setOpaque(false);
            tipoPanel.add(tipoLabel);
            
            headerPanel.add(nameLabel, BorderLayout.WEST);
            headerPanel.add(tipoPanel, BorderLayout.EAST);

            JPanel statsPanel = new JPanel(new GridLayout(1, 3, 5, 0));
            statsPanel.setBorder(new EmptyBorder(0, 10, 5, 10));

            JLabel poderLabel = new JLabel("Poder: " + (poder > 0 ? poder : "-"), JLabel.CENTER);

            JLabel precisionLabel = new JLabel("Precisión: " + (precision > 0 ? precision : "-"), JLabel.CENTER);

            JLabel ppLabel = new JLabel("PP: " + ppActual + "/" + ppMax, JLabel.CENTER);
            
            statsPanel.add(poderLabel);
            statsPanel.add(precisionLabel);
            statsPanel.add(ppLabel);

            movePanel.add(headerPanel, BorderLayout.NORTH);
            movePanel.add(statsPanel, BorderLayout.CENTER);

            movePanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    dialog.dispose();
                    realizarAtaque(movimiento);
                }
                
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    movePanel.setBackground(new Color(240, 240, 255));
                }
                
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    movePanel.setBackground(UIManager.getColor("Panel.background"));
                }
            });
            
            movimientosPanel.add(movePanel);
        }

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(cancelButton);

        mainPanel.add(movimientosPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void realizarAtaque(MovimientoDTO movimiento) {
        PokemonDTO atacante = turnoEntrenador1 ? pokemon1 : pokemon2;
        PokemonDTO defensor = turnoEntrenador1 ? pokemon2 : pokemon1;

        if (movimiento.getPP() <= 0) {
            messageArea.setText("El movimiento " + movimiento.getNombre() + " no tiene PP disponibles.");
            return;
        }

        movimiento.setPPActual(movimiento.getPP() - 1);
        messageArea.setText(atacante.getName() + " usa " + movimiento.getNombre() + "!");

        Timer timer = new Timer(1000, e -> {
            if (atacante.tieneEstado()) {
                String estado = atacante.getStatus();
                if ("confundido".equals(estado) && Math.random() < 1.0/3.0) {
                    int dañoConfusion = Math.max(1, atacante.getMaxPs() / 10);
                    atacante.setPsActual(Math.max(0, atacante.getPs() - dañoConfusion));
                    messageArea.setText(atacante.getName() + " está tan confundido que se hirió a sí mismo!");
                    
                    actualizarInterfaz();
                    
                    Timer confusionTimer = new Timer(1000, evt -> {
                        if (atacante.getPs() <= 0) {
                            atacante.setEstado("debilitado");
                            messageArea.setText(atacante.getName() + " se ha debilitado!");
                            
                            EntrenadorDTO entrenadorAtacante = turnoEntrenador1 ? entrenador1 : entrenador2;
                            Timer cambioTimer = new Timer(1000, evt2 -> {
                                if (tienePokemonDisponible(entrenadorAtacante)) {
                                    mostrarCambioPokemonObligatorio(entrenadorAtacante);
                                } else {
                                    EntrenadorDTO ganador = turnoEntrenador1 ? entrenador2 : entrenador1;
                                    finalizarBatalla(ganador);
                                }
                            });
                            cambioTimer.setRepeats(false);
                            cambioTimer.start();
                            return;
                        }
                        
                        Timer turnoTimer = new Timer(1000, evt2 -> cambiarTurno());
                        turnoTimer.setRepeats(false);
                        turnoTimer.start();
                    });
                    confusionTimer.setRepeats(false);
                    confusionTimer.start();
                    return;
                } else if ("paralizado".equals(estado) && Math.random() < 0.25) {
                    messageArea.setText(atacante.getName() + " está paralizado y no puede moverse!");
                    
                    Timer paralizadoTimer = new Timer(1000, evt -> cambiarTurno());
                    paralizadoTimer.setRepeats(false);
                    paralizadoTimer.start();
                    return;
                } else if ("dormido".equals(estado)) {
                    messageArea.setText(atacante.getName() + " está dormido!");

                    if (Math.random() < 0.2) {
                        Timer despertar = new Timer(1000, evt -> {
                            atacante.setEstado(null);
                            messageArea.setText(atacante.getName() + " se ha despertado!");
                            
                            Timer turnoTimer = new Timer(1000, evt2 -> cambiarTurno());
                            turnoTimer.setRepeats(false);
                            turnoTimer.start();
                        });
                        despertar.setRepeats(false);
                        despertar.start();
                    } else {
                        Timer turnoTimer = new Timer(1000, evt -> cambiarTurno());
                        turnoTimer.setRepeats(false);
                        turnoTimer.start();
                    }
                    return;
                } else if ("congelado".equals(estado)) {
                    messageArea.setText(atacante.getName() + " está congelado y no puede moverse!");

                    if (Math.random() < 0.2) {
                        Timer descongelar = new Timer(1000, evt -> {
                            atacante.setEstado(null);
                            messageArea.setText(atacante.getName() + " se ha descongelado!");
                            
                            Timer turnoTimer = new Timer(1000, evt2 -> cambiarTurno());
                            turnoTimer.setRepeats(false);
                            turnoTimer.start();
                        });
                        descongelar.setRepeats(false);
                        descongelar.start();
                    } else {
                        Timer turnoTimer = new Timer(1000, evt -> cambiarTurno());
                        turnoTimer.setRepeats(false);
                        turnoTimer.start();
                    }
                    return;
                }
            }
            
            boolean acierta = comprobarPrecision(movimiento);
            
            if (acierta) {
                String tipoMovimiento = movimiento.getClaseMovimiento();
                
                if ("Estado".equals(tipoMovimiento)) {
                    String nombreMovimiento = movimiento.getNombre();
                    String mensaje = aplicarEfectoMovimientoEstado(nombreMovimiento, atacante, defensor);
                    messageArea.setText(mensaje);
                } else {
                    int daño = calcularDaño(movimiento, atacante, defensor);
                    if (daño > 0) {
                        defensor.setPsActual(Math.max(0, defensor.getPs() - daño));
                        messageArea.setText(defensor.getName() + " recibió " + daño + " de daño.");

                        aplicarEfectoSecundario(movimiento, atacante, defensor);
                    }
                }

                actualizarInterfaz();

                if (defensor.getPs() <= 0) {
                    defensor.setEstado("debilitado");

                    Timer debilitadoTimer = new Timer(1000, evt -> {
                        messageArea.setText(defensor.getName() + " se ha debilitado!");

                        EntrenadorDTO entrenadorDefensor = turnoEntrenador1 ? entrenador2 : entrenador1;
                        
                        Timer cambioTimer = new Timer(1000, evt2 -> {
                            if (tienePokemonDisponible(entrenadorDefensor)) {
                                mostrarCambioPokemonObligatorio(entrenadorDefensor);
                            } else {
                                EntrenadorDTO ganador = turnoEntrenador1 ? entrenador1 : entrenador2;
                                finalizarBatalla(ganador);
                            }
                        });
                        cambioTimer.setRepeats(false);
                        cambioTimer.start();
                    });
                    debilitadoTimer.setRepeats(false);
                    debilitadoTimer.start();
                    return;
                }
            } else {
                messageArea.setText("¡El ataque ha fallado!");
            }

            Timer efectosEstadoTimer = new Timer(1000, evt -> {
                aplicarEfectosEstadoFinTurno(atacante);
                
                Timer cambioTurnoTimer = new Timer(1000, evt2 -> cambiarTurno());
                cambioTurnoTimer.setRepeats(false);
                cambioTurnoTimer.start();
            });
            efectosEstadoTimer.setRepeats(false);
            efectosEstadoTimer.start();
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private boolean comprobarPrecision(MovimientoDTO movimiento) {
        if (movimiento.getPrecision() == 0) return true;

        double probabilidadAcierto = movimiento.getPrecision() / 100.0;
        return Math.random() <= probabilidadAcierto;
    }
    
    private int calcularDaño(MovimientoDTO movimiento, PokemonDTO atacante, PokemonDTO defensor) {
        int poder = movimiento.getPoder();
        if (poder <= 0) return 0;

        double daño = poder * 0.8;

        daño *= (0.85 + Math.random() * 0.15);
        
        return (int)Math.round(daño);
    }
    
    private void mostrarItems() {
        EntrenadorDTO entrenadorActual = turnoEntrenador1 ? entrenador1 : entrenador2;
        
        if (entrenadorActual.getItems() == null || entrenadorActual.getItems().isEmpty()) {
            messageArea.setText("No tienes objetos para usar.");
            return;
        }

        final JDialog dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Mochila", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 240));
        
        JLabel titleLabel = new JLabel("Selecciona un ítem para usar:", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(new Color(240, 240, 240));
        
        for (ItemDTO item : entrenadorActual.getItems()) {
            JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JPanel imagePanel = new JPanel(new BorderLayout());
            imagePanel.setOpaque(false);
            imagePanel.setPreferredSize(new Dimension(48, 48));

            JLabel imageLabel = new JLabel();
            ImageIcon itemIcon = loadItemImage(item.getTipo());
            if (itemIcon != null) {
                imageLabel.setIcon(itemIcon);
            } else {
                imageLabel.setOpaque(true);
                imageLabel.setBackground(getItemColor(item.getTipo()));
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                imageLabel.setPreferredSize(new Dimension(32, 32));
            }
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imagePanel.add(imageLabel, BorderLayout.CENTER);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);
            
            JLabel nameLabel = new JLabel(item.getNombre());
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JTextArea descLabel = new JTextArea(item.getDescripcion());
            descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            descLabel.setLineWrap(true);
            descLabel.setWrapStyleWord(true);
            descLabel.setEditable(false);
            descLabel.setOpaque(false);
            descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            infoPanel.add(nameLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            infoPanel.add(descLabel);

            itemPanel.add(imagePanel, BorderLayout.WEST);
            itemPanel.add(infoPanel, BorderLayout.CENTER);

            itemPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    dialog.dispose();
                    seleccionarPokemonParaItem(item);
                }
                
                public void mouseEntered(MouseEvent evt) {
                    itemPanel.setBackground(new Color(230, 240, 255));
                }
                
                public void mouseExited(MouseEvent evt) {
                    itemPanel.setBackground(Color.WHITE);
                }
            });
            
            itemsPanel.add(itemPanel);
            itemsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }
        
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelButton.setBackground(BUTTON_COLOR);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(cancelButton);
        
        mainPanel.add(new JScrollPane(itemsPanel), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setSize(350, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    /**
     * Carga la imagen de un ítem según su tipo
     */
    private ImageIcon loadItemImage(String itemType) {
        String path = null;
        
        switch (itemType) {
            case "NormalPotion":
                path = "images/items/normalpotion.png";
                break;
            case "SuperPotion":
                path = "images/items/superpotion.png";
                break;
            case "HyperPotion":
                path = "images/items/hyperpotion.png";
                break;
            case "Revive":
                path = "images/items/revive.png";
                break;
            default:
                path = "images/pokeball.png";
        }

        try {
            java.net.URL imageURL = getClass().getResource("/" + path);
            if (imageURL != null) {
                ImageIcon originalIcon = new ImageIcon(imageURL);
                Image image = originalIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            }
        } catch (Exception e) {}

        try {
            String directPath = System.getProperty("user.dir") + "/" + path;
            java.io.File file = new java.io.File(directPath);
            
            if (file.exists()) {
                ImageIcon originalIcon = new ImageIcon(directPath);
                Image image = originalIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar imagen de ítem: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene un color según el tipo de ítem (usado si la imagen no está disponible)
     */
    private Color getItemColor(String itemType) {
        switch (itemType) {
            case "NormalPotion":
                return new Color(255, 182, 193);
            case "SuperPotion":
                return new Color(255, 105, 180);
            case "HyperPotion":
                return new Color(219, 112, 147);
            case "Revive":
                return new Color(255, 215, 0);
            default:
                return new Color(192, 192, 192);
        }
    }
    
    private void seleccionarPokemonParaItem(ItemDTO item) {
        EntrenadorDTO entrenadorActual = turnoEntrenador1 ? entrenador1 : entrenador2;

        final JDialog dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), 
                                       "Seleccionar Pokémon", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel pokemonesPanel = new JPanel();
        pokemonesPanel.setLayout(new BoxLayout(pokemonesPanel, BoxLayout.Y_AXIS));
        
        boolean hayPokemonValido = false;
        
        for (PokemonDTO pokemon : entrenadorActual.getPokemones()) {
            boolean esValido = true;
            
            if (item.getTipo().contains("Potion") && pokemon.estaDebilitado()) {
                esValido = false;
            } else if (item.getTipo().equals("Revive") && !pokemon.estaDebilitado()) {
                esValido = false;
            } else if (item.getTipo().contains("Potion") && pokemon.getPs() >= pokemon.getMaxPs()) {
                esValido = false;
            }
            
            JPanel pokemonPanel = new JPanel(new BorderLayout());
            pokemonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            
            if (!esValido) {
                pokemonPanel.setBackground(Color.LIGHT_GRAY);
            } else {
                hayPokemonValido = true;
                
                pokemonPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        dialog.dispose();
                        usarItem(item, pokemon);
                    }
                });
            }
            
            JLabel nameLabel = new JLabel(pokemon.getName());
            nameLabel.setFont(PIXEL_FONT);
            
            JLabel psLabel = new JLabel("PS: " + pokemon.getPs() + "/" + pokemon.getMaxPs());
            
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.add(nameLabel);
            infoPanel.add(psLabel);
            
            pokemonPanel.add(infoPanel);
            
            pokemonesPanel.add(pokemonPanel);
            pokemonesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        if (!hayPokemonValido) {
            JLabel mensaje = new JLabel("No hay Pokémon válidos para este objeto.");
            pokemonesPanel.add(mensaje);
        }
        
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(cancelButton);
        
        mainPanel.add(new JScrollPane(pokemonesPanel), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void usarItem(ItemDTO item, PokemonDTO pokemon) {
        EntrenadorDTO entrenadorActual = turnoEntrenador1 ? entrenador1 : entrenador2;
        
        try {
            messageArea.setText(entrenadorActual.getNombre() + " usa " + item.getNombre() + " en " + pokemon.getName() + ".");

            if (item.getTipo().contains("Potion")) {
                int cantidadCuracion;
                switch (item.getTipo()) {
                    case "HyperPotion": cantidadCuracion = 100; break;
                    case "SuperPotion": cantidadCuracion = 50; break;
                    case "NormalPotion": default: cantidadCuracion = 20; break;
                }

                int psAnteriores = pokemon.getPs();
                int nuevosPS = Math.min(pokemon.getPs() + cantidadCuracion, pokemon.getMaxPs());
                pokemon.setPsActual(nuevosPS);
                
                int psCurados = nuevosPS - psAnteriores;

                Timer timer = new Timer(1000, e -> {
                    messageArea.setText("¡" + pokemon.getName() + " recuperó " + psCurados + " PS!");
                    actualizarInterfaz();

                    Timer turnoTimer = new Timer(1000, evt -> cambiarTurno());
                    turnoTimer.setRepeats(false);
                    turnoTimer.start();
                });
                timer.setRepeats(false);
                timer.start();
                
            } else if (item.getTipo().equals("Revive")) {

                int mitadPS = pokemon.getMaxPs() / 2;
                pokemon.setPsActual(mitadPS);
                pokemon.setEstado(null);

                Timer timer = new Timer(1000, e -> {
                    messageArea.setText("¡" + pokemon.getName() + " ha vuelto a la batalla!");
                    actualizarInterfaz();

                    Timer turnoTimer = new Timer(1000, evt -> cambiarTurno());
                    turnoTimer.setRepeats(false);
                    turnoTimer.start();
                });
                timer.setRepeats(false);
                timer.start();
            }

            entrenadorActual.getItems().remove(item);
            
        } catch (Exception e) {
            messageArea.setText("Error al usar el objeto: " + e.getMessage());
            JOptionPane.showMessageDialog(
                this,
                "Error al usar el objeto: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Muestra un diálogo para seleccionar un Pokémon para cambiar
     */
    private void mostrarCambioPokemon() {
        EntrenadorDTO entrenadorActual = turnoEntrenador1 ? entrenador1 : entrenador2;

        if (!tieneMasDeUnPokemonDisponible(entrenadorActual)) {
            messageArea.setText("No tienes más Pokémon disponibles para cambiar.");
            return;
        }

        final JDialog dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Selecciona un Pokémon", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 240));
        
        JLabel titleLabel = new JLabel("Selecciona un Pokémon:", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel pokemonesPanel = new JPanel();
        pokemonesPanel.setLayout(new BoxLayout(pokemonesPanel, BoxLayout.Y_AXIS));
        pokemonesPanel.setBackground(new Color(240, 240, 240));
        
        for (PokemonDTO pokemon : entrenadorActual.getPokemones()) {
            if (!pokemon.estaDebilitado() && 
                (turnoEntrenador1 ? !pokemon.equals(pokemon1) : !pokemon.equals(pokemon2))) {
                
                JPanel pokemonPanel = crearPanelSeleccionPokemon(pokemon, dialog);
                pokemonesPanel.add(pokemonPanel);
                pokemonesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelButton.setBackground(BUTTON_COLOR);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(cancelButton);
        
        mainPanel.add(new JScrollPane(pokemonesPanel), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setSize(350, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    /**
     * Crea un panel para mostrar la información de un Pokémon en el diálogo de selección
     */
    private JPanel crearPanelSeleccionPokemon(PokemonDTO pokemon, JDialog dialog) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);

        JLabel iconoLabel = new JLabel();
        try {
            ImageIcon icon = loadPokemonImage(pokemon.getName().toLowerCase());
            if (icon != null) {
                Image image = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                iconoLabel.setIcon(new ImageIcon(image));
            }
        } catch (Exception e) {
        }
        
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(pokemon.getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JProgressBar hpBar = new JProgressBar(0, pokemon.getMaxPs());
        hpBar.setValue(pokemon.getPs());
        hpBar.setStringPainted(false);

        float porcentaje = (float) pokemon.getPs() / pokemon.getMaxPs();
        if (porcentaje < 0.2) {
            hpBar.setForeground(Color.RED);
        } else if (porcentaje < 0.5) {
            hpBar.setForeground(Color.ORANGE);
        } else {
            hpBar.setForeground(Color.GREEN);
        }
        
        JLabel psLabel = new JLabel("PS: " + pokemon.getPs() + "/" + pokemon.getMaxPs());
        psLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        dataPanel.add(nameLabel);
        dataPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        dataPanel.add(hpBar);
        dataPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        dataPanel.add(psLabel);
        
        infoPanel.add(iconoLabel, BorderLayout.WEST);
        infoPanel.add(dataPanel, BorderLayout.CENTER);
        
        panel.add(infoPanel, BorderLayout.CENTER);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dialog.dispose();
                cambiarPokemon(pokemon);
            }
            
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(240, 240, 255));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.WHITE);
            }
        });
        
        return panel;
    }

    
    private void cambiarPokemon(PokemonDTO nuevoPokemon) {
        if (turnoEntrenador1) {
            pokemon1 = nuevoPokemon;
            messageArea.setText(entrenador1.getNombre() + " cambia a " + pokemon1.getName() + "!");

            ImageIcon icon1 = loadPokemonImageBack(pokemon1.getName().toLowerCase());
            if (icon1 != null) {
                pokemon1ImageLabel.setIcon(icon1);
            }
        } else {
            pokemon2 = nuevoPokemon;
            messageArea.setText(entrenador2.getNombre() + " cambia a " + pokemon2.getName() + "!");

            ImageIcon icon2 = loadPokemonImage(pokemon2.getName().toLowerCase());
            if (icon2 != null) {
                pokemon2ImageLabel.setIcon(icon2);
            }
        }

        ajustarComponentes();

        actualizarInterfaz();

        Timer timer = new Timer(1000, e -> cambiarTurno());
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * Muestra un diálogo para seleccionar un Pokémon cuando uno ha sido debilitado
     * Similar al método mostrarCambioPokemon pero con algunas modificaciones
     */
    private void mostrarCambioPokemonObligatorio(EntrenadorDTO entrenador) {
        if (esCPU(entrenador)) {
            elegirPokemonAutomatico(entrenador);
            return;
        }

        final JDialog dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "¡Pokémon debilitado!", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 240));
        
        JLabel titleLabel = new JLabel(entrenador.getNombre() + ", ¡debes elegir otro Pokémon!", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel pokemonesPanel = new JPanel();
        pokemonesPanel.setLayout(new BoxLayout(pokemonesPanel, BoxLayout.Y_AXIS));
        pokemonesPanel.setBackground(new Color(240, 240, 240));
        
        boolean hayPokemonDisponible = false;
        
        for (PokemonDTO pokemon : entrenador.getPokemones()) {
            if (!pokemon.estaDebilitado()) {
                hayPokemonDisponible = true;

                JPanel pokemonPanel = crearPanelSeleccionPokemonObligatorio(pokemon, dialog, entrenador);
                pokemonesPanel.add(pokemonPanel);
                pokemonesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        if (!hayPokemonDisponible) {
            JLabel mensaje = new JLabel("¡No hay más Pokémon disponibles!");
            mensaje.setFont(new Font("SansSerif", Font.BOLD, 14));
            mensaje.setHorizontalAlignment(SwingConstants.CENTER);
            mensaje.setForeground(Color.RED);
            pokemonesPanel.add(mensaje);

            Timer timer = new Timer(2000, e -> {
                dialog.dispose();
                EntrenadorDTO ganador = entrenador == entrenador1 ? entrenador2 : entrenador1;
                finalizarBatalla(ganador);
            });
            timer.setRepeats(false);
            timer.start();
        }
        
        mainPanel.add(new JScrollPane(pokemonesPanel), BorderLayout.CENTER);
        
        dialog.add(mainPanel);
        dialog.setSize(350, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void elegirPokemonAutomatico(EntrenadorDTO entrenador) {
        List<PokemonDTO> pokemonesDisponibles = new ArrayList<>();
        
        for (PokemonDTO pokemon : entrenador.getPokemones()) {
            if (!pokemon.estaDebilitado()) {
                pokemonesDisponibles.add(pokemon);
            }
        }
        
        if (pokemonesDisponibles.isEmpty()) {
            EntrenadorDTO ganador = entrenador == entrenador1 ? entrenador2 : entrenador1;
            finalizarBatalla(ganador);
            return;
        }

        PokemonDTO pokemonElegido = pokemonesDisponibles.get(random.nextInt(pokemonesDisponibles.size()));
        
        if (entrenador == entrenador1) {
            pokemon1 = pokemonElegido;
            messageArea.setText(entrenador1.getNombre() + " envía a " + pokemon1.getName() + "!");

            ImageIcon icon1 = loadPokemonImageBack(pokemon1.getName().toLowerCase());
            if (icon1 != null) {
                pokemon1ImageLabel.setIcon(icon1);
            }
        } else {
            pokemon2 = pokemonElegido;
            messageArea.setText(entrenador2.getNombre() + " envía a " + pokemon2.getName() + "!");

            ImageIcon icon2 = loadPokemonImage(pokemon2.getName().toLowerCase());
            if (icon2 != null) {
                pokemon2ImageLabel.setIcon(icon2);
            }
        }

        ajustarComponentes();
        actualizarInterfaz();

        Timer timer = new Timer(1500, e -> {
            if (esCPU(entrenador)) {
                jugarTurnoCPU();
            } else {
                cambiarTurno();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * Crea un panel para mostrar la información de un Pokémon en el diálogo de selección obligatorio
     */
    private JPanel crearPanelSeleccionPokemonObligatorio(PokemonDTO pokemon, JDialog dialog, EntrenadorDTO entrenador) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);

        JLabel iconoLabel = new JLabel();
        try {
            ImageIcon icon = loadPokemonImage(pokemon.getName().toLowerCase());
            if (icon != null) {
                Image image = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                iconoLabel.setIcon(new ImageIcon(image));
            }
        } catch (Exception e) {
        }
        
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(pokemon.getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JProgressBar hpBar = new JProgressBar(0, pokemon.getMaxPs());
        hpBar.setValue(pokemon.getPs());
        hpBar.setStringPainted(false);
        
        float porcentaje = (float) pokemon.getPs() / pokemon.getMaxPs();
        if (porcentaje < 0.2) {
            hpBar.setForeground(Color.RED);
        } else if (porcentaje < 0.5) {
            hpBar.setForeground(Color.ORANGE);
        } else {
            hpBar.setForeground(Color.GREEN);
        }
        
        JLabel psLabel = new JLabel("PS: " + pokemon.getPs() + "/" + pokemon.getMaxPs());
        psLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        dataPanel.add(nameLabel);
        dataPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        dataPanel.add(hpBar);
        dataPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        dataPanel.add(psLabel);
        
        infoPanel.add(iconoLabel, BorderLayout.WEST);
        infoPanel.add(dataPanel, BorderLayout.CENTER);
        
        panel.add(infoPanel, BorderLayout.CENTER);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dialog.dispose();
                
                if (entrenador == entrenador1) {
                    pokemon1 = pokemon;
                    messageArea.setText(entrenador1.getNombre() + " envía a " + pokemon1.getName() + "!");

                    ImageIcon icon1 = loadPokemonImageBack(pokemon1.getName().toLowerCase());
                    if (icon1 != null) {
                        pokemon1ImageLabel.setIcon(icon1);
                    }
                } else {
                    pokemon2 = pokemon;
                    messageArea.setText(entrenador2.getNombre() + " envía a " + pokemon2.getName() + "!");

                    ImageIcon icon2 = loadPokemonImage(pokemon2.getName().toLowerCase());
                    if (icon2 != null) {
                        pokemon2ImageLabel.setIcon(icon2);
                    }
                }

                ajustarComponentes();

                actualizarInterfaz();
            }
            
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(240, 240, 255));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.WHITE);
            }
        });
        
        return panel;
    }
    
    private void intentarHuir() {
        EntrenadorDTO entrenadorActual = turnoEntrenador1 ? entrenador1 : entrenador2;
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que quieres huir de la batalla?",
            "Huir",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            messageArea.setText(entrenadorActual.getNombre() + " ha huido de la batalla.");
            Timer timer = new Timer(1500, e -> {
                EntrenadorDTO ganador = turnoEntrenador1 ? entrenador2 : entrenador1;
                finalizarBatalla(ganador);
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    private void cambiarTurno() {
        turnoEntrenador1 = !turnoEntrenador1;
        PokemonDTO pokemonActual = turnoEntrenador1 ? pokemon1 : pokemon2;
        
        if (pokemonActual.estaDebilitado()) {
            EntrenadorDTO entrenadorActual = turnoEntrenador1 ? entrenador1 : entrenador2;
            if (tienePokemonDisponible(entrenadorActual)) {
                mostrarCambioPokemonObligatorio(entrenadorActual);
            } else {
                EntrenadorDTO ganador = turnoEntrenador1 ? entrenador2 : entrenador1;
                finalizarBatalla(ganador);
                return;
            }
        }
        
        EntrenadorDTO entrenadorActual = turnoEntrenador1 ? entrenador1 : entrenador2;

        if (esCPU(entrenadorActual)) {
            Timer timer = new Timer(1000, e -> jugarTurnoCPU());
            timer.setRepeats(false);
            timer.start();
        } else {
            messageArea.setText("¿Qué debería hacer " + pokemonActual.getName() + "?");
        }

        actualizarInterfaz();
    }
    
    private void jugarTurnoCPU() {
        PokemonDTO pokemonCPU = turnoEntrenador1 ? pokemon1 : pokemon2;
        PokemonDTO pokemonOponente = turnoEntrenador1 ? pokemon2 : pokemon1;

        if (pokemonCPU.estaDebilitado()) {
            EntrenadorDTO entrenadorCPU = turnoEntrenador1 ? entrenador1 : entrenador2;
            if (tienePokemonDisponible(entrenadorCPU)) {
                mostrarCambioPokemonObligatorio(entrenadorCPU);
            } else {
                EntrenadorDTO ganador = turnoEntrenador1 ? entrenador2 : entrenador1;
                finalizarBatalla(ganador);
            }
            return;
        }

        messageArea.setText(pokemonCPU.getName() + " está pensando...");

        if (pokemonCPU.tieneEstado()) {
            String estado = pokemonCPU.getStatus();
            
            if ("dormido".equals(estado)) {
                if (Math.random() < 0.2) {
                    pokemonCPU.setEstado(null);
                    messageArea.setText(pokemonCPU.getName() + " se ha despertado!");
                    Timer timer = new Timer(1000, e -> cambiarTurno());
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    messageArea.setText(pokemonCPU.getName() + " está dormido!");
                    Timer timer = new Timer(1000, e -> cambiarTurno());
                    timer.setRepeats(false);
                    timer.start();
                }
                return;
            } else if ("congelado".equals(estado)) {
                if (Math.random() < 0.2) {
                    pokemonCPU.setEstado(null);
                    messageArea.setText(pokemonCPU.getName() + " se ha descongelado!");
                    Timer timer = new Timer(1000, e -> cambiarTurno());
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    messageArea.setText(pokemonCPU.getName() + " está congelado!");
                    Timer timer = new Timer(1000, e -> cambiarTurno());
                    timer.setRepeats(false);
                    timer.start();  
                }
                return;
            } else if ("paralizado".equals(estado) && Math.random() < 0.25) {
                messageArea.setText(pokemonCPU.getName() + " está paralizado y no puede moverse!");
                Timer timer = new Timer(1000, e -> cambiarTurno());
                timer.setRepeats(false);
                timer.start();
                return;
            }
        }

        if (pokemonCPU.getMovimientos() != null && !pokemonCPU.getMovimientos().isEmpty()) {
            List<MovimientoDTO> movimientosDisponibles = new ArrayList<>();
            for (MovimientoDTO mov : pokemonCPU.getMovimientos()) {
                if (mov.getPP() > 0) {
                    movimientosDisponibles.add(mov);
                }
            }
            
            if (!movimientosDisponibles.isEmpty()) {
                MovimientoDTO movElegido = movimientosDisponibles.get(random.nextInt(movimientosDisponibles.size()));
                realizarAtaque(movElegido);
                return;
            }
        }

        EntrenadorDTO entrenadorCPU = turnoEntrenador1 ? entrenador1 : entrenador2;
        List<PokemonDTO> pokemonesDisponibles = new ArrayList<>();
        
        for (PokemonDTO poke : entrenadorCPU.getPokemones()) {
            if (!poke.estaDebilitado() && !poke.equals(pokemonCPU)) {
                pokemonesDisponibles.add(poke);
            }
        }
        
        if (!pokemonesDisponibles.isEmpty()) {
            PokemonDTO nuevoPokemon = pokemonesDisponibles.get(random.nextInt(pokemonesDisponibles.size()));
            cambiarPokemon(nuevoPokemon);
            return;
        }

        messageArea.setText(entrenadorCPU.getNombre() + " no puede hacer nada en este turno.");
        
        Timer timer = new Timer(1000, e -> cambiarTurno());
        timer.setRepeats(false);
        timer.start();
    }
    
    private void finalizarBatalla(EntrenadorDTO ganador) {
        attackButton.setEnabled(false);
        bagButton.setEnabled(false);
        pokemonButton.setEnabled(false);
        runButton.setEnabled(false);

        messageArea.setText("¡" + ganador.getNombre() + " ha ganado la batalla!");
        Timer timer = new Timer(2500, e -> {
            JOptionPane.showMessageDialog(
                this,
                "¡" + ganador.getNombre() + " ha ganado la batalla!",
                "Fin de la Batalla",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            mainGUI.showMainMenu();
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void actualizarInterfaz() {
        nameLabel1.setText(pokemon1.getName());
        hpBar1.setValue(pokemon1.getPs());
        hpBar1.setMaximum(pokemon1.getMaxPs());
        hpLabel1.setText(pokemon1.getPs() + "/" + pokemon1.getMaxPs());

        nameLabel2.setText(pokemon2.getName());
        hpBar2.setValue(pokemon2.getPs());
        hpBar2.setMaximum(pokemon2.getMaxPs());
        hpLabel2.setText(pokemon2.getPs() + "/" + pokemon2.getMaxPs());

        if (pokemon1.tieneEstado()) {
            nameLabel1.setText(pokemon1.getName() + " [" + pokemon1.getStatus() + "]");
        }
        
        if (pokemon2.tieneEstado()) {
            nameLabel2.setText(pokemon2.getName() + " [" + pokemon2.getStatus() + "]");
        }

        EntrenadorDTO entrenadorActual = turnoEntrenador1 ? entrenador1 : entrenador2;
        boolean esTurnoHumano = !esCPU(entrenadorActual);
        
        attackButton.setEnabled(esTurnoHumano);
        bagButton.setEnabled(esTurnoHumano);
        pokemonButton.setEnabled(esTurnoHumano);
        runButton.setEnabled(esTurnoHumano);

        revalidate();
        repaint();
    }
    
    private boolean tienePokemonDisponible(EntrenadorDTO entrenador) {
        for (PokemonDTO pokemon : entrenador.getPokemones()) {
            if (!pokemon.estaDebilitado()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean tieneMasDeUnPokemonDisponible(EntrenadorDTO entrenador) {
        int disponibles = 0;
        for (PokemonDTO pokemon : entrenador.getPokemones()) {
            if (!pokemon.estaDebilitado()) {
                disponibles++;
                if (disponibles > 1) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private PokemonDTO obtenerPrimerPokemonDisponible(EntrenadorDTO entrenador) {
        if (entrenador == null || entrenador.getPokemones() == null) return null;
        
        for (PokemonDTO pokemon : entrenador.getPokemones()) {
            if (!pokemon.estaDebilitado()) {
                return pokemon;
            }
        }
        
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
    private Color getTypeColor(String tipo) {
        switch (tipo.toLowerCase()) {
            case "normal": return new Color(168, 168, 120);
            case "fuego": case "fire": return new Color(240, 128, 48);
            case "agua": case "water": return new Color(104, 144, 240);
            case "planta": case "grass": return new Color(120, 200, 80);
            case "eléctrico": case "electric": return new Color(248, 208, 48);
            case "hielo": case "ice": return new Color(152, 216, 216);
            case "lucha": case "fighting": return new Color(192, 48, 40);
            case "veneno": case "poison": return new Color(160, 64, 160);
            case "tierra": case "ground": return new Color(224, 192, 104);
            case "volador": case "flying": return new Color(168, 144, 240);
            case "psíquico": case "psychic": return new Color(248, 88, 136);
            case "bicho": case "bug": return new Color(168, 184, 32);
            case "roca": case "rock": return new Color(184, 160, 56);
            case "fantasma": case "ghost": return new Color(112, 88, 152);
            case "dragón": case "dragon": return new Color(112, 56, 248);
            case "siniestro": case "dark": return new Color(112, 88, 72);
            case "acero": case "steel": return new Color(184, 184, 208);
            case "hada": case "fairy": return new Color(238, 153, 172);
            default: return new Color(128, 128, 128);
        }
    }
    
    private void aplicarEfectosEstadoFinTurno(PokemonDTO pokemon) {
        if (!pokemon.tieneEstado()) return;
        
        String estado = pokemon.getStatus();
        String mensaje = "";
        
        if ("quemado".equals(estado)) {
            int dañoQuemadura = Math.max(1, pokemon.getMaxPs() / 8);
            pokemon.setPsActual(Math.max(0, pokemon.getPs() - dañoQuemadura));
            mensaje = pokemon.getName() + " sufre " + dañoQuemadura + " PS de daño por quemadura";
        } else if ("envenenado".equals(estado)) {
            int dañoVeneno = Math.max(1, pokemon.getMaxPs() / 8);
            pokemon.setPsActual(Math.max(0, pokemon.getPs() - dañoVeneno));
            mensaje = pokemon.getName() + " sufre " + dañoVeneno + " PS de daño por veneno";
        } else if ("gravemente envenenado".equals(estado)) {
            int dañoVeneno = Math.max(1, pokemon.getMaxPs() / 6);
            pokemon.setPsActual(Math.max(0, pokemon.getPs() - dañoVeneno));
            mensaje = pokemon.getName() + " sufre " + dañoVeneno + " PS de daño por envenenamiento grave";
        }
        
        if (!mensaje.isEmpty()) {
            messageArea.setText(mensaje);
            actualizarInterfaz();

            if (pokemon.getPs() <= 0) {
                pokemon.setEstado("debilitado");
                
                Timer debilitadoTimer = new Timer(1000, e -> {
                    messageArea.setText(pokemon.getName() + " se ha debilitado por su estado!");
                    
                    EntrenadorDTO entrenadorAfectado = (pokemon == pokemon1) ? entrenador1 : entrenador2;
                    
                    if (tienePokemonDisponible(entrenadorAfectado)) {
                        mostrarCambioPokemonObligatorio(entrenadorAfectado);
                    } else {
                        EntrenadorDTO ganador = (pokemon == pokemon1) ? entrenador2 : entrenador1;
                        finalizarBatalla(ganador);
                    }
                });
                debilitadoTimer.setRepeats(false);
                debilitadoTimer.start();
            }
        }
    }

    private void aplicarEfectoSecundario(MovimientoDTO movimiento, PokemonDTO atacante, PokemonDTO defensor) {
        if (defensor.tieneEstado() || defensor.estaDebilitado()) return;
        
        String nombreMovimiento = movimiento.getNombre();
        double random = Math.random();

        if (("Lanzallamas".equals(nombreMovimiento) || "Rueda Fuego".equals(nombreMovimiento)) && random < 0.1) {
            defensor.setEstado("quemado");
            messageArea.setText(defensor.getName() + " ha sido quemado!");
        } else if (("Rayo".equals(nombreMovimiento) || "Colmillo Trueno".equals(nombreMovimiento)) && random < 0.1) {
            defensor.setEstado("paralizado");
            messageArea.setText(defensor.getName() + " ha sido paralizado!");
        } else if (("Rayo Hielo".equals(nombreMovimiento) || "Puño Hielo".equals(nombreMovimiento)) && random < 0.1) {
            defensor.setEstado("congelado");
            messageArea.setText(defensor.getName() + " ha sido congelado!");
        } else if (("Bomba Lodo".equals(nombreMovimiento) || "Puya Nociva".equals(nombreMovimiento)) && random < 0.3) {
            defensor.setEstado("envenenado");
            messageArea.setText(defensor.getName() + " ha sido envenenado!");
        } else if ("Golpe Cuerpo".equals(nombreMovimiento) && random < 0.3) {
            defensor.setEstado("paralizado");
            messageArea.setText(defensor.getName() + " ha sido paralizado!");
        } else if ("Huracán".equals(nombreMovimiento) && random < 0.3) {
            defensor.setEstado("confundido");
            messageArea.setText(defensor.getName() + " ha sido confundido!");
        }
    }

    private String aplicarEfectoMovimientoEstado(String nombreMovimiento, PokemonDTO usuario, PokemonDTO objetivo) {
        String resultado = "";
        
        switch (nombreMovimiento) {
            case "Tóxico":
                if (!objetivo.tieneEstado()) {
                    objetivo.setEstado("gravemente envenenado");
                    resultado = "¡" + objetivo.getName() + " ha sido gravemente envenenado!";
                } else {
                    resultado = "No tuvo efecto en " + objetivo.getName();
                }
                break;
                
            case "Hipnosis":
                if (!objetivo.tieneEstado()) {
                    objetivo.setEstado("dormido");
                    resultado = "¡" + objetivo.getName() + " se ha dormido!";
                } else {
                    resultado = "No tuvo efecto en " + objetivo.getName();
                }
                break;
                
            case "Danza Dragon":
                resultado = "¡" + usuario.getName() + " aumentó su ataque y velocidad!";
                break;
                
            case "Refugio":
                resultado = "¡" + usuario.getName() + " aumentó su defensa!";
                break;
                
            case "Paralizador":
            case "Onda Trueno":
                if (!objetivo.tieneEstado()) {
                    objetivo.setEstado("paralizado");
                    resultado = "¡" + objetivo.getName() + " ha sido paralizado!";
                } else {
                    resultado = "No tuvo efecto en " + objetivo.getName();
                }
                break;
                
            case "Rayo Confuso":
                if (!objetivo.tieneEstado()) {
                    objetivo.setEstado("confundido");
                    resultado = "¡" + objetivo.getName() + " ha sido confundido!";
                } else {
                    resultado = "No tuvo efecto en " + objetivo.getName();
                }
                break;
                
            case "Respiro":
                int restaurarPs = Math.max(1, usuario.getMaxPs() / 2);
                usuario.setPsActual(Math.min(usuario.getPs() + restaurarPs, usuario.getMaxPs()));
                resultado = "¡" + usuario.getName() + " ha restaurado " + restaurarPs + " PS!";
                break;
                
            case "Fuegofatuo":
                if (!objetivo.tieneEstado()) {
                    objetivo.setEstado("quemado");
                    resultado = "¡" + objetivo.getName() + " ha sido quemado!";
                } else {
                    resultado = "No tuvo efecto en " + objetivo.getName();
                }
                break;
                
            default:
                resultado = "Se aplicó " + nombreMovimiento;
                break;
        }
        
        return resultado;
    }
    
    /**
     * Muestra el menú de pausa con opciones para guardar la partida
     */
    private void mostrarMenuPausa() {
        JDialog pauseDialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Menú de Pausa", true);
        pauseDialog.setLayout(new BorderLayout());
        
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton guardarButton = new JButton("Guardar Partida");
        JButton continuarButton = new JButton("Continuar Batalla");
        JButton salirButton = new JButton("Salir al Menú Principal");
        
        guardarButton.addActionListener(ev -> {
            pauseDialog.dispose();
            mainGUI.guardarEstadoBatalla(
                entrenador1.getNombre(),
                entrenador2.getNombre(),
                pokemon1, 
                pokemon2, 
                turnoEntrenador1
            );
        });
        
        continuarButton.addActionListener(ev -> pauseDialog.dispose());
        
        salirButton.addActionListener(ev -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                pauseDialog,
                "¿Estás seguro de salir? Se perderá el progreso no guardado.",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                pauseDialog.dispose();
                mainGUI.showMainMenu();
            }
        });
        
        menuPanel.add(guardarButton);
        menuPanel.add(continuarButton);
        menuPanel.add(salirButton);
        
        pauseDialog.add(menuPanel, BorderLayout.CENTER);
        pauseDialog.pack();
        pauseDialog.setLocationRelativeTo(this);
        pauseDialog.setResizable(false);
        pauseDialog.setVisible(true);
    }

    /**
     * Restaura el estado de la batalla a partir de los datos guardados
     * @param pokemon1Guardado Pokémon activo del entrenador 1
     * @param pokemon2Guardado Pokémon activo del entrenador 2
     * @param turnoE1 Indica si es el turno del entrenador 1
     */
    public void restaurarEstadoGuardado(PokemonDTO pokemon1Guardado, PokemonDTO pokemon2Guardado, boolean turnoE1) {
        if (pokemon1Guardado != null) {
            pokemon1 = pokemon1Guardado;
            ImageIcon icon1 = loadPokemonImageBack(pokemon1.getName().toLowerCase());
            if (icon1 != null) {
                pokemon1ImageLabel.setIcon(icon1);
            }
        }
        
        if (pokemon2Guardado != null) {
            pokemon2 = pokemon2Guardado;

            ImageIcon icon2 = loadPokemonImage(pokemon2.getName().toLowerCase());
            if (icon2 != null) {
                pokemon2ImageLabel.setIcon(icon2);
            }
        }
        turnoEntrenador1 = turnoE1;

        actualizarInterfaz();
        ajustarComponentes();

        PokemonDTO pokemonActual = turnoEntrenador1 ? pokemon1 : pokemon2;
        messageArea.setText("¿Qué debería hacer " + pokemonActual.getName() + "?");
    }
    
    private boolean esCPU(EntrenadorDTO entrenador) {
        if (entrenador == null) return false;
        String nombre = entrenador.getNombre().toUpperCase();
        return nombre.contains("CPU") || nombre.contains("MÁQUINA") || nombre.contains("MACHINE");
    }
}