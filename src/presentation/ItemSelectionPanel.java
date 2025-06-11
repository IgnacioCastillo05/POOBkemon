package presentation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;


import presentation.dto.*;

/**
 * Panel simplificado para la selección de ítems sin efectos visuales complejos
 * para evitar problemas de superposición y glitches visuales
 */
public class ItemSelectionPanel extends PoobkemonPanel {
    private JLabel titleLabel;
    private JPanel itemCardsPanel;
    private JPanel selectedItemsPanel;
    private JButton confirmButton, cancelButton;
    
    private List<ItemDTO> itemsSeleccionados;
    private String nombreEntrenador;
    private final int MAX_ITEMS = 7;

    private static final Color COLOR_POTION = new Color(255, 182, 193);
    private static final Color COLOR_SUPER_POTION = new Color(255, 105, 180);
    private static final Color COLOR_HYPER_POTION = new Color(219, 112, 147);
    private static final Color COLOR_REVIVE = new Color(255, 215, 0);

    private int countNormalPotion = 0;
    private int countSuperPotion = 0;
    private int countHyperPotion = 0;
    private int countRevive = 0;

    private JLabel counterLabelNormalPotion;
    private JLabel counterLabelSuperPotion;
    private JLabel counterLabelHyperPotion;
    private JLabel counterLabelRevive;
    
    public ItemSelectionPanel(PoobkemonGUI mainGUI, String nombreEntrenador) {
        super(mainGUI);
        this.nombreEntrenador = nombreEntrenador;
        this.itemsSeleccionados = new ArrayList<>();
        initialize();
    }
    
    @Override
    protected void prepareElements() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel(nombreEntrenador + ", elige hasta " + MAX_ITEMS + " ítems para tu entrenador (0/" + MAX_ITEMS + ")", 
                               JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        JLabel rulesLabel = new JLabel("Reglas: Máximo 2 pociones de cada tipo y máximo 1 revivir", JLabel.CENTER);
        rulesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        rulesLabel.setForeground(Color.DARK_GRAY);
        topPanel.add(rulesLabel, BorderLayout.SOUTH);
        
        selectedItemsPanel = new JPanel(new GridLayout(1, 4, 10, 5));
        selectedItemsPanel.setBackground(new Color(240, 240, 240));
        selectedItemsPanel.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Ítems seleccionados"
            )
        );

        JPanel potionPanel = createCounterPanel("Poción Normal", COLOR_POTION);
        JPanel superPotionPanel = createCounterPanel("Super Poción", COLOR_SUPER_POTION);
        JPanel hyperPotionPanel = createCounterPanel("Hiper Poción", COLOR_HYPER_POTION);
        JPanel revivePanel = createCounterPanel("Revivir", COLOR_REVIVE);
        
        selectedItemsPanel.add(potionPanel);
        selectedItemsPanel.add(superPotionPanel);
        selectedItemsPanel.add(hyperPotionPanel);
        selectedItemsPanel.add(revivePanel);

        counterLabelNormalPotion = (JLabel) ((JPanel)potionPanel.getComponent(1)).getComponent(0);
        counterLabelSuperPotion = (JLabel) ((JPanel)superPotionPanel.getComponent(1)).getComponent(0);
        counterLabelHyperPotion = (JLabel) ((JPanel)hyperPotionPanel.getComponent(1)).getComponent(0);
        counterLabelRevive = (JLabel) ((JPanel)revivePanel.getComponent(1)).getComponent(0);

        JPanel selectedItemsContainer = new JPanel(new BorderLayout());
        selectedItemsContainer.setBackground(new Color(245, 245, 245));
        selectedItemsContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        selectedItemsContainer.add(selectedItemsPanel, BorderLayout.CENTER);
        
        topPanel.add(selectedItemsContainer, BorderLayout.NORTH);

        itemCardsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        itemCardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        itemCardsPanel.setBackground(new Color(250, 250, 250));

        itemCardsPanel.add(createSimpleItemCard(
            "NormalPotion", 
            "Poción Normal", 
            "Restaura 20 PS del Pokémon",
            COLOR_POTION
        ));
        
        itemCardsPanel.add(createSimpleItemCard(
            "SuperPotion", 
            "Super Poción", 
            "Restaura 50 PS del Pokémon",
            COLOR_SUPER_POTION
        ));
        
        itemCardsPanel.add(createSimpleItemCard(
            "HyperPotion", 
            "Hiper Poción", 
            "Restaura 100 PS del Pokémon",
            COLOR_HYPER_POTION
        ));
        
        itemCardsPanel.add(createSimpleItemCard(
            "Revive", 
            "Revivir", 
            "Revive a un Pokémon debilitado con la mitad de sus PS máximos",
            COLOR_REVIVE
        ));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        confirmButton = new JButton("Confirmar Ítems");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton skipButton = new JButton("No agregar ítems");
        skipButton.setFont(new Font("Arial", Font.PLAIN, 14));
        skipButton.addActionListener(e -> confirmItems());
        
        bottomPanel.add(skipButton);
        bottomPanel.add(cancelButton);
        bottomPanel.add(confirmButton);

        add(topPanel, BorderLayout.NORTH);
        add(itemCardsPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Crea un panel para mostrar un contador de ítem
     */
    private JPanel createCounterPanel(String nombre, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(color.darker(), 1));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 70));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, color.darker()));
        
        JLabel nameLabel = new JLabel(nombre, JLabel.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel limitLabel = new JLabel();
        if (nombre.equals("Revivir")) {
            limitLabel.setText("(Máx. 1)");
        } else {
            limitLabel.setText("(Máx. 2)");
        }
        limitLabel.setFont(new Font("Arial", Font.ITALIC, 9));
        limitLabel.setHorizontalAlignment(JLabel.CENTER);
        
        headerPanel.add(nameLabel, BorderLayout.CENTER);
        headerPanel.add(limitLabel, BorderLayout.SOUTH);

        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        countPanel.setBackground(Color.WHITE);
        
        JLabel countLabel = new JLabel("0");
        countLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        countPanel.add(countLabel);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(countPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crea una tarjeta visual simplificada para un ítem
     */
    private JPanel createSimpleItemCard(String tipo, String nombre, String descripcion, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(color.darker(), 2));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 70));
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, color.darker()));
        
        JLabel titleLabel = new JLabel(nombre, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titlePanel.add(titleLabel);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 5));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel iconLabel = new JLabel(loadItemImage(tipo));
        iconLabel.setHorizontalAlignment(JLabel.CENTER);

        JTextArea descriptionArea = new JTextArea(descripcion);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel limitLabel = new JLabel();
        if (tipo.equals("Revive")) {
            limitLabel.setText("(Máximo 1)");
        } else {
            limitLabel.setText("(Máximo 2)");
        }
        limitLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        limitLabel.setForeground(Color.DARK_GRAY);
        limitLabel.setHorizontalAlignment(JLabel.RIGHT);

        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setOpaque(false);
        descPanel.add(descriptionArea, BorderLayout.CENTER);
        descPanel.add(limitLabel, BorderLayout.SOUTH);
        
        contentPanel.add(iconLabel, BorderLayout.WEST);
        contentPanel.add(descPanel, BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, color.darker()));

        JButton minusButton = new JButton("-");
        minusButton.setFont(new Font("Arial", Font.BOLD, 14));
        minusButton.setPreferredSize(new Dimension(50, 30));
        minusButton.setEnabled(false);

        JLabel counterLabel = new JLabel("0", JLabel.CENTER);
        counterLabel.setFont(new Font("Arial", Font.BOLD, 14));
        counterLabel.setPreferredSize(new Dimension(50, 30));
        counterLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JButton plusButton = new JButton("+");
        plusButton.setFont(new Font("Arial", Font.BOLD, 14));
        plusButton.setPreferredSize(new Dimension(50, 30));

        ItemDTO itemDTO = createItemDTO(tipo, nombre, descripcion);

        plusButton.addActionListener(e -> {
            if (getItemsCount() >= MAX_ITEMS) {
                JOptionPane.showMessageDialog(
                    this,
                    "Has alcanzado el límite de " + MAX_ITEMS + " ítems en total.",
                    "Límite Alcanzado",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
            int currentCount = Integer.parseInt(counterLabel.getText());
            if ((tipo.equals("NormalPotion") && currentCount >= 2) ||
                (tipo.equals("SuperPotion") && currentCount >= 2) ||
                (tipo.equals("HyperPotion") && currentCount >= 2)) {
                JOptionPane.showMessageDialog(
                    this,
                    "Solo puedes seleccionar máximo 2 pociones de " + nombre + ".",
                    "Límite Alcanzado",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            if (tipo.equals("Revive") && currentCount >= 1) {
                JOptionPane.showMessageDialog(
                    this,
                    "Solo puedes seleccionar máximo 1 " + nombre + ".",
                    "Límite Alcanzado",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            currentCount++;
            counterLabel.setText(String.valueOf(currentCount));
            minusButton.setEnabled(true);

            if ((tipo.equals("Revive") && currentCount >= 1) ||
                (!tipo.equals("Revive") && currentCount >= 2)) {
                plusButton.setEnabled(false);
            }

            itemsSeleccionados.add(itemDTO);

            updateGlobalCounter(tipo, true);
        });
        
        minusButton.addActionListener(e -> {
            int count = Integer.parseInt(counterLabel.getText());
            if (count > 0) {
                count--;
                counterLabel.setText(String.valueOf(count));
                if (count == 0) {
                    minusButton.setEnabled(false);
                }

                plusButton.setEnabled(true);

                for (int i = 0; i < itemsSeleccionados.size(); i++) {
                    if (itemsSeleccionados.get(i).getTipo().equals(tipo)) {
                        itemsSeleccionados.remove(i);
                        break;
                    }
                }

                updateGlobalCounter(tipo, false);
            }
        });
        
        controlPanel.add(minusButton);
        controlPanel.add(counterLabel);
        controlPanel.add(plusButton);

        card.add(titlePanel, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);
        card.add(controlPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    /**
     * Crea un objeto ItemDTO
     */
    private ItemDTO createItemDTO(String tipo, String nombre, String descripcion) {
        ItemDTO item = new ItemDTO();
        item.setTipo(tipo);
        item.setNombre(nombre);
        item.setDescripcion(descripcion);
        return item;
    }
    
    /**
     * Carga la imagen de un ítem
     */
    private ImageIcon loadItemImage(String tipo) {
        String imagePath = "/images/items/";
        switch (tipo) {
            case "NormalPotion":
                imagePath += "potion.png";
                break;
            case "SuperPotion":
                imagePath += "superpotion.png";
                break;
            case "HyperPotion":
                imagePath += "hyperpotion.png";
                break;
            case "Revive":
                imagePath += "revive.png";
                break;
            default:
                imagePath += "item_default.png";
        }

        try {
            URL imageURL = getClass().getResource(imagePath);
            if (imageURL != null) {
                ImageIcon originalIcon = new ImageIcon(imageURL);
                Image image = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar imagen " + imagePath + ": " + e.getMessage());
        }

        String[] alternativePaths = {
            "/" + imagePath,
            imagePath.substring(1),
            "../" + imagePath,
            "images/items/" + tipo.toLowerCase() + ".png"
        };
        
        for (String path : alternativePaths) {
            try {
                URL imageURL = getClass().getResource(path);
                if (imageURL != null) {
                    ImageIcon originalIcon = new ImageIcon(imageURL);
                    Image image = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                    return new ImageIcon(image);
                }
            } catch (Exception e) {
            }
        }

        try {
            String directPath = System.getProperty("user.dir") + "/images/items/" + tipo.toLowerCase() + ".png";
            File file = new File(directPath);
            
            if (file.exists()) {
                ImageIcon originalIcon = new ImageIcon(directPath);
                Image image = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar desde sistema de archivos: " + e.getMessage());
        }

        return createColorIcon(tipo);
    }

    /**
     * Crea un ícono de color como respaldo si no se encuentra la imagen
     */
    private ImageIcon createColorIcon(String tipo) {
        Color color;
        switch (tipo) {
            case "NormalPotion":
                color = COLOR_POTION;
                break;
            case "SuperPotion":
                color = COLOR_SUPER_POTION;
                break;
            case "HyperPotion":
                color = COLOR_HYPER_POTION;
                break;
            case "Revive":
                color = COLOR_REVIVE;
                break;
            default:
                color = Color.GRAY;
        }

        int size = 64;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        String label;
        switch (tipo) {
            case "NormalPotion":
                label = "P";
                break;
            case "SuperPotion":
                label = "SP";
                break;
            case "HyperPotion":
                label = "HP";
                break;
            case "Revive":
                label = "R";
                break;
            default:
                label = "?";
        }
        g2d.setColor(color);
        g2d.fillOval(4, 4, size - 8, size - 8);

        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(4, 4, size - 8, size - 8);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics metrics = g2d.getFontMetrics();
        int x = (size - metrics.stringWidth(label)) / 2;
        int y = ((size - metrics.getHeight()) / 2) + metrics.getAscent();
        g2d.drawString(label, x, y);
        
        g2d.dispose();
        
        return new ImageIcon(image);
    }
    
    
    /**
     * Actualiza los contadores globales
     */
    private void updateGlobalCounter(String tipo, boolean increment) {
        switch (tipo) {
            case "NormalPotion":
                countNormalPotion = increment ? countNormalPotion + 1 : countNormalPotion - 1;
                counterLabelNormalPotion.setText(String.valueOf(countNormalPotion));
                break;
            case "SuperPotion":
                countSuperPotion = increment ? countSuperPotion + 1 : countSuperPotion - 1;
                counterLabelSuperPotion.setText(String.valueOf(countSuperPotion));
                break;
            case "HyperPotion":
                countHyperPotion = increment ? countHyperPotion + 1 : countHyperPotion - 1;
                counterLabelHyperPotion.setText(String.valueOf(countHyperPotion));
                break;
            case "Revive":
                countRevive = increment ? countRevive + 1 : countRevive - 1;
                counterLabelRevive.setText(String.valueOf(countRevive));
                break;
        }

        titleLabel.setText(nombreEntrenador + ", elige hasta " + MAX_ITEMS + " ítems para tu entrenador (" + getItemsCount() + "/" + MAX_ITEMS + ")");
    }
    
    /**
     * Obtiene el número total de ítems seleccionados
     */
    private int getItemsCount() {
        return itemsSeleccionados.size();
    }
    
    /**
     * Confirma la selección de ítems
     */
    private void confirmItems() {
        mainGUI.setItemsSeleccionados(nombreEntrenador, itemsSeleccionados);
        
        JOptionPane.showMessageDialog(
            this,
            "Has seleccionado " + itemsSeleccionados.size() + " ítems para " + nombreEntrenador + ".",
            "Selección completada",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        mainGUI.entrenadorListo(nombreEntrenador);
    }
    
    @Override
    protected void prepareActions() {
        confirmButton.addActionListener(e -> confirmItems());
        
        cancelButton.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cancelar la selección de ítems?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION
            );
            
            if (opcion == JOptionPane.YES_OPTION) {
                mainGUI.showMainMenu();
            }
        });
    }
}