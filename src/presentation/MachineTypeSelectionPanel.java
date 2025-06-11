package presentation;

import java.awt.*;
import javax.swing.*;

public class MachineTypeSelectionPanel extends PoobkemonPanel {
    private JLabel titleLabel;
    private JPanel buttonPanel;
    private JButton attackingButton, defensiveButton, changingButton, expertButton, backButton;
    private int modalidadJuego;

    public MachineTypeSelectionPanel(PoobkemonGUI mainGUI, int modalidadJuego) {
        super(mainGUI);
        this.modalidadJuego = modalidadJuego;
        initialize();
    }

    @Override
    protected void prepareElements() {
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 0));

        String titulo = (modalidadJuego == PoobkemonGUI.MODALIDAD_PVM) ? 
            "Selecciona el tipo de CPU oponente" : 
            "Selecciona el tipo de CPU 1";
            
        titleLabel = new JLabel(titulo, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 20));
        buttonPanel.setBackground(new Color(0, 0, 0, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        attackingButton = new JButton("Ofensivo - Prioriza ataques poderosos");
        attackingButton.setFont(new Font("Arial", Font.BOLD, 16));
        attackingButton.setToolTipText("CPU que busca causar el máximo daño posible");
        
        defensiveButton = new JButton("Defensivo - Enfoque en resistencia");
        defensiveButton.setFont(new Font("Arial", Font.BOLD, 16));
        defensiveButton.setToolTipText("CPU que prioriza curación y movimientos de estado");
        
        changingButton = new JButton("Cambiante - Cambia Pokémon frecuentemente");
        changingButton.setFont(new Font("Arial", Font.BOLD, 16));
        changingButton.setToolTipText("CPU que cambia constantemente buscando ventaja de tipo");
        
        expertButton = new JButton("Experto - Combina todas las estrategias");
        expertButton.setFont(new Font("Arial", Font.BOLD, 16));
        expertButton.setToolTipText("CPU más inteligente que adapta su estrategia");
        
        backButton = new JButton("Volver");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));

        buttonPanel.add(attackingButton);
        buttonPanel.add(defensiveButton);
        buttonPanel.add(changingButton);
        buttonPanel.add(expertButton);
        buttonPanel.add(backButton);

        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    protected void prepareActions() {
        attackingButton.addActionListener(e -> {
            if (modalidadJuego == PoobkemonGUI.MODALIDAD_PVM) {
                mainGUI.setTipoMaquina("attacking");
                mainGUI.showTrainerSetup();
            } else { // MvM
                mainGUI.setTipoMaquina1("attacking");
                mainGUI.showMachineType2Selection();
            }
        });
        
        defensiveButton.addActionListener(e -> {
            if (modalidadJuego == PoobkemonGUI.MODALIDAD_PVM) {
                mainGUI.setTipoMaquina("defensive");
                mainGUI.showTrainerSetup();
            } else { // MvM
                mainGUI.setTipoMaquina1("defensive");
                mainGUI.showMachineType2Selection();
            }
        });
        
        changingButton.addActionListener(e -> {
            if (modalidadJuego == PoobkemonGUI.MODALIDAD_PVM) {
                mainGUI.setTipoMaquina("changing");
                mainGUI.showTrainerSetup();
            } else { // MvM
                mainGUI.setTipoMaquina1("changing");
                mainGUI.showMachineType2Selection();
            }
        });
        
        expertButton.addActionListener(e -> {
            if (modalidadJuego == PoobkemonGUI.MODALIDAD_PVM) {
                mainGUI.setTipoMaquina("expert");
                mainGUI.showTrainerSetup();
            } else { // MvM
                mainGUI.setTipoMaquina1("expert");
                mainGUI.showMachineType2Selection();
            }
        });
        
        backButton.addActionListener(e -> mainGUI.showBattleType());
    }
}