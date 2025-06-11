package presentation;

import java.awt.*;
import javax.swing.*;

public class TrainerSetupPanel extends PoobkemonPanel {
    private JLabel titleLabel;
    private JPanel formPanel;
    private JLabel trainer1Label, trainer2Label;
    private JTextField trainer1Field, trainer2Field;
    private JButton startButton, backButton;
    
    public TrainerSetupPanel(PoobkemonGUI mainGUI) {
        super(mainGUI);
        initialize();
    }

    @Override
    protected void prepareElements() {
        setLayout(new BorderLayout());

        titleLabel = new JLabel("Configuración de Entrenadores", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 20));
        formPanel.setBackground(new Color(0, 0, 0, 0));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        trainer1Label = new JLabel("Nombre del Entrenador 1:");
        trainer1Label.setFont(new Font("Arial", Font.BOLD, 14));
        trainer1Label.setForeground(Color.WHITE);
        
        trainer1Field = new JTextField("Jugador 1");
        trainer1Field.setFont(new Font("Arial", Font.PLAIN, 14));
        
        trainer2Label = new JLabel("Nombre del Entrenador 2:");
        trainer2Label.setFont(new Font("Arial", Font.BOLD, 14));
        trainer2Label.setForeground(Color.WHITE);
        
        trainer2Field = new JTextField("Jugador 2");
        trainer2Field.setFont(new Font("Arial", Font.PLAIN, 14));

        startButton = new JButton("Iniciar Batalla");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        
        backButton = new JButton("Volver");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));

        formPanel.add(trainer1Label);
        formPanel.add(trainer1Field);
        formPanel.add(trainer2Label);
        formPanel.add(trainer2Field);
        formPanel.add(new JLabel());
        formPanel.add(new JLabel());
        formPanel.add(startButton);
        formPanel.add(backButton);
        
        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }

    @Override
    protected void prepareActions() {
        startButton.addActionListener(e -> {
            String name1 = trainer1Field.getText().trim();
            String name2 = trainer2Field.getText().trim();
            
            if (name1.isEmpty()) {
                name1 = trainer1Field.getText();
            }
            
            if (name2.isEmpty()) {
                name2 = trainer2Field.getText();
            }
            
            mainGUI.startBattle(name1, name2);
        });
        
        backButton.addActionListener(e -> {
            mainGUI.showBattleType();
        });
    }
    
    /**
     * Actualiza las etiquetas y campos de los entrenadores según la modalidad
     * @param modalidad La modalidad de batalla seleccionada
     */
    public void updateTrainerLabels(int modalidad) {
        switch (modalidad) {
            case PoobkemonGUI.MODALIDAD_PVP:
                trainer1Label.setText("Nombre del Entrenador 1:");
                trainer2Label.setText("Nombre del Entrenador 2:");
                trainer1Field.setText("Jugador 1");
                trainer2Field.setText("Jugador 2");
                trainer1Field.setEnabled(true);
                trainer2Field.setEnabled(true);
                break;
                
            case PoobkemonGUI.MODALIDAD_PVM:
                trainer1Label.setText("Nombre del Jugador:");
                trainer2Label.setText("Nombre de la CPU:");
                trainer1Field.setText("Jugador");
                trainer2Field.setText("CPU");
                trainer1Field.setEnabled(true);
                trainer2Field.setEnabled(false);
                break;
                
            case PoobkemonGUI.MODALIDAD_MVM:
                trainer1Label.setText("Nombre de la CPU 1:");
                trainer2Label.setText("Nombre de la CPU 2:");
                trainer1Field.setText("CPU 1");
                trainer2Field.setText("CPU 2");
                trainer1Field.setEnabled(false);
                trainer2Field.setEnabled(false);
                break;
        }
    }
}