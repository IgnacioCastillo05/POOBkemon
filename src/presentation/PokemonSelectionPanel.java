package presentation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.LineBorder;

import presentation.dto.*;


public class PokemonSelectionPanel extends PoobkemonPanel {
    private JLabel titleLabel;
    private JPanel cardsPanel;
    private JButton confirmButton, cancelButton;
    private JPanel selectedPokemonPanel;
    
    private List<PokemonDTO> pokemonesDisponibles;
    private List<PokemonDTO> pokemonesSeleccionados;
    private List<JPanel> selectedCardPanels;
    private String nombreEntrenador;
    private int maxSelections;

    private static final Color COLOR_NORMAL = new Color(168, 168, 120);
    private static final Color COLOR_FIRE = new Color(240, 128, 48);
    private static final Color COLOR_WATER = new Color(104, 144, 240);
    private static final Color COLOR_GRASS = new Color(120, 200, 80);
    private static final Color COLOR_ELECTRIC = new Color(248, 208, 48);
    private static final Color COLOR_ICE = new Color(152, 216, 216);
    private static final Color COLOR_FIGHTING = new Color(192, 48, 40);
    private static final Color COLOR_POISON = new Color(160, 64, 160);
    private static final Color COLOR_GROUND = new Color(224, 192, 104);
    private static final Color COLOR_FLYING = new Color(168, 144, 240);
    private static final Color COLOR_PSYCHIC = new Color(248, 88, 136);
    private static final Color COLOR_BUG = new Color(168, 184, 32);
    private static final Color COLOR_ROCK = new Color(184, 160, 56);
    private static final Color COLOR_GHOST = new Color(112, 88, 152);
    private static final Color COLOR_DRAGON = new Color(112, 56, 248);
    private static final Color COLOR_DARK = new Color(112, 88, 72);
    private static final Color COLOR_STEEL = new Color(184, 184, 208);
    private static final Color COLOR_FAIRY = new Color(238, 153, 172);

    private static final LineBorder NORMAL_BORDER = new LineBorder(Color.GRAY, 1);
    
    public PokemonSelectionPanel(PoobkemonGUI mainGUI, String nombreEntrenador, int maxSelections) {
        super(mainGUI);
        this.nombreEntrenador = nombreEntrenador;
        this.maxSelections = maxSelections;
        this.pokemonesDisponibles = mainGUI.getPokemonesDisponiblesDTO();
        this.pokemonesSeleccionados = new ArrayList<>();
        this.selectedCardPanels = new ArrayList<>();
        initialize();
    }
    
    @Override
    protected void prepareElements() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel(nombreEntrenador + ", elige " + maxSelections + " Pokémon para tu equipo", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        selectedPokemonPanel = new JPanel(new GridLayout(1, maxSelections, 10, 5));
        selectedPokemonPanel.setBackground(new Color(230, 230, 230));
        selectedPokemonPanel.setBorder(BorderFactory.createTitledBorder("Equipo seleccionado"));

        for (int i = 0; i < maxSelections; i++) {
            JPanel placeholderPanel = createEmptySlot(i + 1);
            selectedPokemonPanel.add(placeholderPanel);
        }
        
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(selectedPokemonPanel, BorderLayout.CENTER);

        cardsPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cardsPanel.setBackground(Color.WHITE);
        
        for (PokemonDTO pokemon : pokemonesDisponibles) {
            JPanel card = createSimplePokemonCard(pokemon);
            cardsPanel.add(card);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        confirmButton = new JButton("Confirmar Equipo");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setEnabled(false);
        
        cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        bottomPanel.add(cancelButton);
        bottomPanel.add(confirmButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(cardsPanel), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Crea un marcador de posición vacío para el equipo
     */
    private JPanel createEmptySlot(int position) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(100, 120));
        panel.setBorder(NORMAL_BORDER);
        panel.setBackground(new Color(240, 240, 240));
        
        JLabel label = new JLabel("#" + position, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.GRAY);
        
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Crea una tarjeta visual ultra simple para un Pokémon - sin efectos de hover
     */
    private JPanel createSimplePokemonCard(PokemonDTO pokemon) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());

        Color bgColor = getTypeColor(pokemon.getTipo1());
        Color lightBgColor = new Color(
            Math.min(255, bgColor.getRed() + 70),
            Math.min(255, bgColor.getGreen() + 70),
            Math.min(255, bgColor.getBlue() + 70),
            80
        );

        card.setBackground(lightBgColor);
        card.setBorder(NORMAL_BORDER);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        
        ImageIcon pokemonIcon = loadPokemonImage(pokemon.getName().toLowerCase());
        JLabel imageLabel = new JLabel(pokemonIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(pokemon.getName(), JLabel.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel typesPanel = new JPanel();
        typesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        typesPanel.setOpaque(false);

        JLabel type1Label = createTypeLabel(pokemon.getTipo1());
        typesPanel.add(type1Label);

        if (pokemon.getTipo2() != null && !pokemon.getTipo2().isEmpty()) {
            JLabel type2Label = createTypeLabel(pokemon.getTipo2());
            typesPanel.add(type2Label);
        }

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(typesPanel);

        card.add(imagePanel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (pokemonesSeleccionados.size() < maxSelections) {
                    PokemonDTO pokemonCopy = createCopy(pokemon);
                    pokemonesSeleccionados.add(pokemonCopy);

                    updateSelectedPanel();

                    if (pokemonesSeleccionados.size() >= 1) {
                        confirmButton.setEnabled(true);
                    }
                }
            }
        });
        
        return card;
    }
    
    /**
     * Crea una copia de un PokemonDTO
     */
    private PokemonDTO createCopy(PokemonDTO original) {
        PokemonDTO copy = new PokemonDTO();
        copy.setNombre(original.getName());
        copy.setPsActual(original.getPs());
        copy.setPsMaximo(original.getMaxPs());
        copy.setTipo1(original.getTipo1());
        copy.setTipo2(original.getTipo2());
        copy.setAttack(original.getAttack());
        copy.setDefense(original.getDefense());
        copy.setMovimientos(original.getMovimientos());
        return copy;
    }
    
    /**
     * Crea una etiqueta para mostrar el tipo de un Pokémon
     */
    private JLabel createTypeLabel(String tipo) {
        JLabel typeLabel = new JLabel(tipo);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 11));
        typeLabel.setForeground(Color.WHITE);
        typeLabel.setOpaque(true);
        typeLabel.setBackground(getTypeColor(tipo));
        typeLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        return typeLabel;
    }
    
    /**
     * Actualiza el panel de Pokémon seleccionados
     */
    private void updateSelectedPanel() {
        SwingUtilities.invokeLater(() -> {
            selectedPokemonPanel.removeAll();
            selectedCardPanels.clear();

            for (int i = 0; i < maxSelections; i++) {
                if (i < pokemonesSeleccionados.size()) {
                    PokemonDTO pokemon = pokemonesSeleccionados.get(i);
                    JPanel miniCard = createSelectedPokemonCard(pokemon, i);
                    selectedPokemonPanel.add(miniCard);
                    selectedCardPanels.add(miniCard);
                } else {

                    JPanel emptySlot = createEmptySlot(i + 1);
                    selectedPokemonPanel.add(emptySlot);
                }
            }

            if (pokemonesSeleccionados.size() < maxSelections) {
                titleLabel.setText(nombreEntrenador + ", elige " + (maxSelections - pokemonesSeleccionados.size()) + " Pokémon más para tu equipo");
            } else {
                titleLabel.setText("¡Equipo completo! Puedes confirmar o cambiar tu selección");
            }
            selectedPokemonPanel.revalidate();
            selectedPokemonPanel.repaint();
        });
    }
    
    /**
     * Crea una tarjeta miniatura para un Pokémon seleccionado
     */
    private JPanel createSelectedPokemonCard(PokemonDTO pokemon, int position) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setPreferredSize(new Dimension(100, 120));

        Color bgColor = getTypeColor(pokemon.getTipo1());
        Color lightBgColor = new Color(
            Math.min(255, bgColor.getRed() + 70),
            Math.min(255, bgColor.getGreen() + 70),
            Math.min(255, bgColor.getBlue() + 70),
            40
        );
        
        panel.setBackground(lightBgColor);
        panel.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 2));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(bgColor.darker());
        
        JLabel positionLabel = new JLabel("#" + (position + 1), JLabel.CENTER);
        positionLabel.setFont(new Font("Arial", Font.BOLD, 11));
        positionLabel.setForeground(Color.WHITE);
        headerPanel.add(positionLabel, BorderLayout.CENTER);

        JButton removeButton = new JButton("×");
        removeButton.setFont(new Font("Arial", Font.BOLD, 12));
        removeButton.setMargin(new Insets(0, 0, 0, 0));
        removeButton.setContentAreaFilled(false);
        removeButton.setBorderPainted(false);
        removeButton.setFocusPainted(false);
        removeButton.setForeground(Color.RED);

        final int indexToRemove = position;
        removeButton.addActionListener(e -> {
            if (indexToRemove >= 0 && indexToRemove < pokemonesSeleccionados.size()) {
                pokemonesSeleccionados.remove(indexToRemove);
                updateSelectedPanel();
                if (pokemonesSeleccionados.size() >= 1) {
                    confirmButton.setEnabled(true);
                } else {
                	confirmButton.setEnabled(false);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(removeButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);

        ImageIcon icon = loadPokemonImage(pokemon.getName().toLowerCase());
        if (icon != null) {
            Image scaledImage = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(imageLabel, BorderLayout.CENTER);
        }

        JLabel nameLabel = new JLabel(pokemon.getName(), JLabel.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(nameLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Obtiene el color correspondiente a un tipo de Pokémon
     */
    private Color getTypeColor(String tipo) {
        if (tipo == null) return COLOR_NORMAL;
        
        switch (tipo.toLowerCase()) {
            case "normal": return COLOR_NORMAL;
            case "fire": case "fuego": return COLOR_FIRE;
            case "water": case "agua": return COLOR_WATER;
            case "grass": case "planta": return COLOR_GRASS;
            case "electric": case "eléctrico": return COLOR_ELECTRIC;
            case "ice": case "hielo": return COLOR_ICE;
            case "fighting": case "lucha": return COLOR_FIGHTING;
            case "poison": case "veneno": return COLOR_POISON;
            case "ground": case "tierra": return COLOR_GROUND;
            case "flying": case "volador": return COLOR_FLYING;
            case "psychic": case "psíquico": return COLOR_PSYCHIC;
            case "bug": case "bicho": return COLOR_BUG;
            case "rock": case "roca": return COLOR_ROCK;
            case "ghost": case "fantasma": return COLOR_GHOST;
            case "dragon": case "dragón": return COLOR_DRAGON;
            case "dark": case "siniestro": return COLOR_DARK;
            case "steel": case "acero": return COLOR_STEEL;
            case "fairy": case "hada": return COLOR_FAIRY;
            default: return COLOR_NORMAL;
        }
    }
    
    /**
     * Carga la imagen de un Pokémon
     */
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
                    ImageIcon originalIcon = new ImageIcon(imageURL);
                    Image image = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    return new ImageIcon(image);
                }
            } catch (Exception e) {}
        }
        
        try {
            String directPath = System.getProperty("user.dir") + "/images/pokemonsFront/" + pokemonName + ".png";
            java.io.File file = new java.io.File(directPath);
            
            if (file.exists()) {
                ImageIcon originalIcon = new ImageIcon(directPath);
                Image image = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar desde sistema de archivos: " + e.getMessage());
        }

        try {
            return new ImageIcon(getClass().getResource("/images/pokeball.png"));
        } catch (Exception e) {

            return new ImageIcon();
        }
    }
    
    @Override
    protected void prepareActions() {
        confirmButton.addActionListener(e -> {
            if (pokemonesSeleccionados.size() >= 1) {
                if (pokemonesSeleccionados.size() < maxSelections) {
                    int opcion = JOptionPane.showConfirmDialog(
                        this,
                        "Solo has seleccionado " + pokemonesSeleccionados.size() + 
                        " de " + maxSelections + " Pokémon posibles. ¿Deseas continuar?",
                        "Confirmar selección",
                        JOptionPane.YES_NO_OPTION
                    );
                    
                    if (opcion != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                mainGUI.setPokemonesSeleccionados(nombreEntrenador, pokemonesSeleccionados);
                mainGUI.showItemSelection(nombreEntrenador);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar al menos 1 Pokémon para continuar.",
                    "Selección incompleta",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
        
        cancelButton.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cancelar la selección?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION
            );
            
            if (opcion == JOptionPane.YES_OPTION) {
                mainGUI.showMainMenu();
            }
        });
    }
}