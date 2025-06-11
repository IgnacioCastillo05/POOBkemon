package presentation;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import presentation.dto.PokemonDTO;

public class PokemonListPanel extends PoobkemonPanel {
    private JButton backButton;
    private JPanel pokemonList;
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox<String> typeFilterComboBox;
    private List<JPanel> allPokemonEntries;

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

    private static final String[] TIPOS_POKEMON = {
    	    "Todos los tipos", "Bug", "Dark", "Dragon", "Electric", "Fairy", 
    	    "Fighting", "Fire", "Flying", "Ghost", "Grass", "Ground", 
    	    "Ice", "Normal", "Poison", "Psychic", "Rock", "Steel", "Water"
    	};
    
    public PokemonListPanel(PoobkemonGUI mainGUI) {
        super(mainGUI);
        initialize();
    }

    @Override
    protected void prepareElements() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 180, 30));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Pokédex");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        typeFilterComboBox = new JComboBox<>(TIPOS_POKEMON);
        typeFilterComboBox.setPreferredSize(new Dimension(140, 30));
        typeFilterComboBox.setToolTipText("Filtrar por tipo");
        
        searchField = new JTextField(15);
        searchField.setPreferredSize(new Dimension(150, 30));
        searchButton = new JButton("Buscar");
        searchButton.setBackground(new Color(255, 203, 5));
        searchButton.setForeground(Color.BLACK);
        searchButton.setFocusPainted(false);
        
        JLabel tipoLabel = new JLabel("Tipo:");
        tipoLabel.setForeground(Color.WHITE);
        searchPanel.add(tipoLabel);
        searchPanel.add(typeFilterComboBox);
        
        JLabel buscarLabel = new JLabel("Buscar:");
        buscarLabel.setForeground(Color.WHITE);
        searchPanel.add(buscarLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        pokemonList = new JPanel();
        pokemonList.setLayout(new BoxLayout(pokemonList, BoxLayout.Y_AXIS));
        pokemonList.setBackground(new Color(245, 245, 245));
        pokemonList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        allPokemonEntries = new ArrayList<>();
        
        JScrollPane scrollPane = new JScrollPane(pokemonList);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(245, 245, 245));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(70, 130, 180));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        backButton = new JButton("Volver al Menú");
        backButton.setBackground(new Color(255, 203, 5));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Carga la lista de Pokémon disponibles desde el sistema
     */
    public void loadPokemonsList() {
        pokemonList.removeAll();
        allPokemonEntries.clear();
        
        List<PokemonDTO> pokemonesDisponibles = mainGUI.getPokemonesDisponiblesDTO();
        
        if (pokemonesDisponibles == null || pokemonesDisponibles.isEmpty()) {
            mostrarMensajeSinDatos();
            return;
        }
        
        for (PokemonDTO pokemon : pokemonesDisponibles) {
            String nombre = pokemon.getName();
            String tipo = pokemon.getTipo1();
            if (pokemon.getTipo2() != null && !pokemon.getTipo2().isEmpty()) {
                tipo += "/" + pokemon.getTipo2();
            }

            JPanel entry = createPokemonEntry(pokemon, nombre, tipo);
            allPokemonEntries.add(entry);
            
            pokemonList.add(entry);
            pokemonList.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        pokemonList.revalidate();
        pokemonList.repaint();
    }
    
    /**
     * Muestra un mensaje cuando no hay datos disponibles
     */
    private void mostrarMensajeSinDatos() {
        JLabel noDataLabel = new JLabel("No hay Pokémones disponibles en el sistema");
        noDataLabel.setFont(new Font("Arial", Font.BOLD, 14));
        noDataLabel.setForeground(Color.RED);
        noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton reloadButton = new JButton("Recargar datos");
        reloadButton.setBackground(new Color(255, 203, 5));
        reloadButton.setForeground(Color.BLACK);
        reloadButton.setFocusPainted(false);
        reloadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        reloadButton.addActionListener(e -> loadPokemonsList());
        
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(new Color(245, 245, 245));
        messagePanel.add(Box.createVerticalGlue());
        messagePanel.add(noDataLabel);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        messagePanel.add(reloadButton);
        messagePanel.add(Box.createVerticalGlue());
        
        pokemonList.add(messagePanel);
    }

    @Override
    protected void prepareActions() {
        backButton.addActionListener(e -> mainGUI.showMainMenu());
        searchButton.addActionListener(e -> filterPokemonList());
        searchField.addActionListener(e -> filterPokemonList());
        typeFilterComboBox.addActionListener(e -> filterPokemonList());
    }
    
    /**
     * Filtra la lista de Pokémon según el texto en el campo de búsqueda y el tipo seleccionado
     */
    private void filterPokemonList() {
        String searchText = searchField.getText().toLowerCase();
        String selectedType = (String) typeFilterComboBox.getSelectedItem();
       
        pokemonList.removeAll();
        if (allPokemonEntries.isEmpty()) {
            mostrarMensajeSinDatos();
            return;
        }
        
        boolean encontrado = false;      
        for (JPanel entry : allPokemonEntries) {
            PokemonDTO pokemon = (PokemonDTO) entry.getClientProperty("pokemon");
            
            if (pokemon == null) continue;
            
            String pokemonName = pokemon.getName().toLowerCase();
            String tipo1 = pokemon.getTipo1();
            String tipo2 = pokemon.getTipo2();

            boolean cumpleFiltroTipo = selectedType.equals("Todos los tipos");
            
            if (!cumpleFiltroTipo) {
                String tipoSeleccionadoNormalizado = traductor(selectedType);
                String tipo1Normalizado = traductor(tipo1);
                String tipo2Normalizado = (tipo2 != null) ? traductor(tipo2) : "";
                
                cumpleFiltroTipo = tipo1Normalizado.equals(tipoSeleccionadoNormalizado) || 
                                 tipo2Normalizado.equals(tipoSeleccionadoNormalizado);
            }
            
            boolean cumpleFiltroTexto = searchText.isEmpty() || pokemonName.contains(searchText);
            
            if (cumpleFiltroTipo && cumpleFiltroTexto) {
                pokemonList.add(entry);
                pokemonList.add(Box.createRigidArea(new Dimension(0, 10)));
                encontrado = true;
            }
        }
        
        if (!encontrado) {
            JLabel noMatchLabel = new JLabel("No se encontraron Pokémones que coincidan con lo que buscas :(");
            noMatchLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            noMatchLabel.setFont(new Font("Arial", Font.BOLD, 14));
            noMatchLabel.setForeground(new Color(180, 0, 0));
            
            JPanel messagePanel = new JPanel();
            messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
            messagePanel.setBackground(new Color(245, 245, 245));
            messagePanel.add(Box.createVerticalGlue());
            messagePanel.add(noMatchLabel);
            messagePanel.add(Box.createVerticalGlue());
            
            pokemonList.add(messagePanel);
        }
        
        pokemonList.revalidate();
        pokemonList.repaint();
    }

    /**
     * Pequeño traductor
     */
    private String traductor(String tipo) {
        if (tipo == null) return "";

        String tipoLower = tipo.toLowerCase();

        switch (tipoLower) {
            case "fire": return "fuego";
            case "water": return "agua";
            case "grass": return "planta";
            case "electric": return "electrico";
            case "ice": return "hielo";
            case "fighting": return "lucha";
            case "poison": return "veneno";
            case "ground": return "tierra";
            case "flying": return "volador";
            case "psychic": return "psiquico";
            case "bug": return "bicho";
            case "rock": return "roca";
            case "ghost": return "fantasma";
            case "dark": return "siniestro";
            case "steel": return "acero";
            case "fairy": return "hada";
            
            default: return tipoLower;
        }
    }

    /**
     * Crea una entrada visual para un Pokémon con diseño mejorado
     */
    private JPanel createPokemonEntry(PokemonDTO pokemon, String name, String typeDisplay) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        Color bgColor = getTypeColor(pokemon.getTipo1());
        Color lightBgColor = new Color(
            Math.min(255, bgColor.getRed() + 70),
            Math.min(255, bgColor.getGreen() + 70),
            Math.min(255, bgColor.getBlue() + 70)
        );
        
        panel.setBackground(lightBgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        panel.putClientProperty("pokemon", pokemon);

        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(70, 70));
        
        ImageIcon pokemonIcon = loadPokemonImage(name.toLowerCase());
        JLabel iconLabel = new JLabel(pokemonIcon);
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        iconPanel.add(iconLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);

        JPanel nameTypePanel = new JPanel(new BorderLayout(10, 0));
        nameTypePanel.setOpaque(false);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        typePanel.setOpaque(false);
        
        JLabel typeLabel = new JLabel(createTypeTag(typeDisplay));
        typePanel.add(typeLabel);
        
        nameTypePanel.add(nameLabel, BorderLayout.WEST);
        nameTypePanel.add(typePanel, BorderLayout.EAST);

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setOpaque(false);

        if (pokemon.getPs() > 0) {
            JPanel statLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            statLine.setOpaque(false);
            JLabel psLabel = new JLabel("PS: " + pokemon.getPs() + "/" + pokemon.getMaxPs());
            psLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            statLine.add(psLabel);
            statsPanel.add(statLine);
        }
        
        infoPanel.add(nameTypePanel, BorderLayout.CENTER);

        JButton infoButton = new JButton("Ver detalles");
        infoButton.setBackground(new Color(70, 130, 180));
        infoButton.setForeground(Color.WHITE);
        infoButton.setFocusPainted(false);
        infoButton.addActionListener(e -> mostrarDetallesPokemon(pokemon));
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);
        actionPanel.add(infoButton);
        
        panel.add(iconPanel, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.EAST);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                panel.setBackground(bgColor);
                nameLabel.setForeground(Color.WHITE);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                panel.setBackground(lightBgColor);
                nameLabel.setForeground(Color.BLACK);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDetallesPokemon(pokemon);
            }
        });
        
        return panel;
    }
    
    /**
     * Crea una etiqueta visual para el tipo de Pokémon
     */
    private String createTypeTag(String typeText) {
        String[] types = typeText.split("/");
        StringBuilder html = new StringBuilder("<html>");
        
        for (String type : types) {
            Color typeColor = getTypeColor(type.trim());
            String hexColor = String.format("#%02X%02X%02X", 
                typeColor.getRed(), typeColor.getGreen(), typeColor.getBlue());
            
            html.append("<span style='background-color:").append(hexColor).append(";")
                .append("color:white;")
                .append("padding:2px 6px;")
                .append("margin-right:4px;")
                .append("border-radius:3px;'>")
                .append(type.trim())
                .append("</span> ");
        }
        
        html.append("</html>");
        return html.toString();
    }
    
    /**
     * Muestra un diálogo con los detalles del Pokémon
     */
    private void mostrarDetallesPokemon(PokemonDTO pokemon) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Detalles de " + pokemon.getName());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getTypeColor(pokemon.getTipo1()));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel nameLabel = new JLabel(pokemon.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setForeground(Color.WHITE);
        headerPanel.add(nameLabel, BorderLayout.WEST);
        
        String typesText = pokemon.getTipo1();
        if (pokemon.getTipo2() != null && !pokemon.getTipo2().isEmpty()) {
            typesText += "/" + pokemon.getTipo2();
        }
        JLabel typesLabel = new JLabel(createTypeTag(typesText));
        headerPanel.add(typesLabel, BorderLayout.EAST);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createLineBorder(getTypeColor(pokemon.getTipo1()).darker(), 2));
        
        ImageIcon pokemonIcon = loadPokemonImage(pokemon.getName().toLowerCase());
        JLabel imageLabel = new JLabel(pokemonIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel statsTitle = new JLabel("Estadísticas");
        statsTitle.setFont(new Font("Arial", Font.BOLD, 18));
        statsTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        statsPanel.add(statsTitle);

        addStatBar(statsPanel, "PS", pokemon.getPs(), pokemon.getMaxPs(), Color.GREEN);
        addStatBar(statsPanel, "Ataque", pokemon.getAttack(), 150, new Color(240, 128, 48));
        addStatBar(statsPanel, "Defensa", pokemon.getDefense(), 150, new Color(104, 144, 240));
        
        if (pokemon.getMovimientos() != null && !pokemon.getMovimientos().isEmpty()) {
            JLabel movesTitle = new JLabel("Movimientos");
            movesTitle.setFont(new Font("Arial", Font.BOLD, 18));
            movesTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
            statsPanel.add(movesTitle);
            
            JPanel movesPanel = new JPanel(new GridLayout(0, 1, 0, 5));
            movesPanel.setBackground(Color.WHITE);
            
            for (int i = 0; i < Math.min(4, pokemon.getMovimientos().size()); i++) {
                presentation.dto.MovimientoDTO move = pokemon.getMovimientos().get(i);
                JLabel moveLabel = new JLabel(move.getNombre() + " (" + move.getTipo() + ")");
                moveLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                movesPanel.add(moveLabel);
            }
            
            statsPanel.add(movesPanel);
        }

        JButton closeButton = new JButton("Cerrar");
        closeButton.setBackground(getTypeColor(pokemon.getTipo1()));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(imagePanel, BorderLayout.WEST);
        contentPanel.add(statsPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setContentPane(contentPanel);
        dialog.setVisible(true);
    }
    
    /**
     * Añade una barra de estadística al panel
     */
    private void addStatBar(JPanel panel, String statName, int value, int maxValue, Color barColor) {
        JPanel statPanel = new JPanel(new BorderLayout(10, 0));
        statPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(statName + ":");
        nameLabel.setPreferredSize(new Dimension(80, 25));
        
        JProgressBar statBar = new JProgressBar(0, maxValue);
        statBar.setValue(value);
        statBar.setStringPainted(true);
        statBar.setString(String.valueOf(value));
        statBar.setForeground(barColor);
        
        statPanel.add(nameLabel, BorderLayout.WEST);
        statPanel.add(statBar, BorderLayout.CENTER);
        
        panel.add(statPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    private ImageIcon loadPokemonImage(String pokemonName) {
        pokemonName = pokemonName.toLowerCase();

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
                    Image image = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    return new ImageIcon(image);
                }
            } catch (Exception e) {}
        }
        try {
            String directPath = System.getProperty("user.dir") + "/images/pokemonsFront/" + pokemonName + ".png";
            File file = new File(directPath);
            
            if (file.exists()) {
                ImageIcon originalIcon = new ImageIcon(directPath);
                Image image = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar desde sistema de archivos: " + e.getMessage());
        }

        try {
            java.net.URL imageURL = getClass().getResource("/images/pokeball.png");
            if (imageURL != null) {
                ImageIcon originalIcon = new ImageIcon(imageURL);
                Image image = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen por defecto");
        }
        return createDefaultIcon();
    }
    
    /**
     * Retorna el color correspondiente a un tipo de Pokémon
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
            case "ghost": case "fantasma": case "fastasma": return COLOR_GHOST;
            case "dragon": case "dragón": return COLOR_DRAGON;
            case "dark": case "siniestro": return COLOR_DARK;
            case "steel": case "acero": return COLOR_STEEL;
            case "fairy": case "hada": return COLOR_FAIRY;
            default: return COLOR_NORMAL;
        }
    }
    
    /**
     * Crea un icono por defecto básico cuando no se encuentra ninguna imagen
     */
    private ImageIcon createDefaultIcon() {
        int width = 50;
        int height = 50;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillOval(0, 0, width, height);
        g2.setColor(Color.DARK_GRAY);
        g2.drawOval(0, 0, width - 1, height - 1);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        g2.drawString("?", width / 2 - 7, height / 2 + 8);
        g2.dispose();
        
        return new ImageIcon(image);
    }
}