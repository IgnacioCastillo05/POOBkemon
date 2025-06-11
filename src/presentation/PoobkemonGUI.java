package presentation;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import domain.*;
import presentation.dto.*;

public class PoobkemonGUI extends JFrame {
    private int ancho, alto;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private MainMenuPanel mainMenuPanel;
    private PokemonListPanel pokemonListPanel;
    private BattleTypePanel battleTypePanel;
    private TrainerSetupPanel trainerSetupPanel;
    private PokemonSelectionPanel pokemonSelectionPanel;
    private ItemSelectionPanel itemSelectionPanel;
    private ImagePanel backgroundPanel;

    private BattlePanel battlePanel;

    private int modoJuego;
    private int modalidadJuego;
    private Entrenador entrenador1Configurado;
    private Entrenador entrenador2Configurado;
    private boolean entrenador1Listo = false;
    private boolean entrenador2Listo = false;
    private boolean datosInicializados = false;
    
    public static final int MODO_NORMAL = 1;
    public static final int MODO_SUPERVIVENCIA = 2;
    
    public static final int MODALIDAD_PVP = 1;
    public static final int MODALIDAD_PVM = 2;
    public static final int MODALIDAD_MVM = 3;
    
    private MachineTypeSelectionPanel machineTypeSelectionPanel;
    private MachineType2SelectionPanel machineType2SelectionPanel;
    private String tipoMaquina1;
    private String tipoMaquina2;
    
    public PoobkemonGUI() {
    	inicializarDatos();
    	prepareElements();
        prepareActions();
    }

    private void prepareElements() {
        setTitle("POOBkemon");
        Dimension frame = Toolkit.getDefaultToolkit().getScreenSize();
        ancho = frame.width/2;
        alto = frame.height/2;
        setSize(ancho, alto);
        setLocation((frame.width - ancho)/2, (frame.height - alto)/2);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        backgroundPanel = new ImagePanel("images/background/pantalla_inicio.png");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        mainMenuPanel = new MainMenuPanel(this);
        pokemonListPanel = new PokemonListPanel(this);
        battleTypePanel = new BattleTypePanel(this);
        trainerSetupPanel = new TrainerSetupPanel(this);
        battlePanel = null;
        machineTypeSelectionPanel = null;
        machineType2SelectionPanel = null;

        cardPanel.add(mainMenuPanel, "mainMenu");
        cardPanel.add(pokemonListPanel, "pokemonList");
        cardPanel.add(battleTypePanel, "battleType");
        cardPanel.add(trainerSetupPanel, "trainerSetup");

        backgroundPanel.add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, "mainMenu");
    }
    
    private void prepareActions() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                confirmarSalida();
            }
        });
    }

    public void showMainMenu() {
        cardLayout.show(cardPanel, "mainMenu");

        entrenador1Configurado = null;
        entrenador2Configurado = null;
        entrenador1Listo = false;
        entrenador2Listo = false;
    }
    
    public void showPokemonList() {
        pokemonListPanel.loadPokemonsList();
    	cardLayout.show(cardPanel, "pokemonList");
    }
    
    public void showBattleType() {
        cardLayout.show(cardPanel, "battleType");
    }
    
    public void showTrainerSetup() {
        trainerSetupPanel.updateTrainerLabels(modalidadJuego);
        cardLayout.show(cardPanel, "trainerSetup");
    }
    
    private void iniciarBatalla() {
        try {
            battlePanel = new BattlePanel(this);
            cardPanel.add(battlePanel, "battle");
            cardLayout.show(cardPanel, "battle");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al iniciar la batalla: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
            showMainMenu();
        }
    }
    
    public void startGame(int modoJuego) {
        this.modoJuego = modoJuego;

        boolean initialized = MainGame.iniciarJuegoGUI(modoJuego);
        
        if (!initialized) {
            JOptionPane.showMessageDialog(
                this,
                "No se pudieron cargar los datos necesarios para el juego. Verifique que el archivo pokemones.bin exista.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        if (modoJuego == MODO_SUPERVIVENCIA) {
            modalidadJuego = MODALIDAD_PVP;
            showTrainerSetup();
        } else {
            showBattleType();
        }
    }
    
    public void setModalidadJuego(int modalidad) {
        this.modalidadJuego = modalidad;
         
        if (modalidad == MODALIDAD_MVM) {
            showMachineTypeSelection();
        } else if (modalidad == MODALIDAD_PVM) {
            showMachineTypeSelection();
        } else {
            showTrainerSetup();
        }
    }
    
    public void startBattle(String nombreEntrenador1, String nombreEntrenador2) {
        try {
            if (!MainGame.iniciarJuegoGUI(modoJuego)) {
                JOptionPane.showMessageDialog(
                    this,
                    "No se pudieron cargar los datos necesarios para el juego.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            boolean inicializacionExitosa = MainGame.iniciarBatallaGUI(
                modoJuego, 
                modalidadJuego, 
                nombreEntrenador1, 
                nombreEntrenador2
            );
            
            if (!inicializacionExitosa) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error durante la inicialización de la batalla.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            entrenador1Configurado = MainGame.getEntrenador1();
            entrenador2Configurado = MainGame.getEntrenador2();

            if (modoJuego == MODO_SUPERVIVENCIA) {
                entrenador1Listo = true;
                entrenador2Listo = true;
                iniciarBatalla();
            } else if (modalidadJuego == MODALIDAD_MVM) {
                entrenador1Listo = true;
                entrenador2Listo = true;
                iniciarBatalla();
            } else if (modalidadJuego == MODALIDAD_PVP) {
                showPokemonSelection(entrenador1Configurado.getNombre(), 6);
            } else if (modalidadJuego == MODALIDAD_PVM) {
                if (entrenador2Configurado.getPokemones().isEmpty()) {
                    ArrayList<Pokemon> pokemonesDisponibles = MainGame.getPokemonesDisponibles();
                    MainGame.seleccionarEquipoAutomatico(entrenador2Configurado, pokemonesDisponibles, 6);
                    MainGame.agregarItemsAutomatico(entrenador2Configurado);
                }
                entrenador2Listo = true;
                showPokemonSelection(entrenador1Configurado.getNombre(), 6);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error durante la batalla: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
            showMainMenu();
        }
    }
    
    public void confirmarSalida() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que quieres salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }
    
    public int getAncho() {
        return ancho;
    }
    
    public int getAlto() {
        return alto;
    }
    
    /**
     * Convierte un Entrenador de dominio a EntrenadorDTO
     */
    private EntrenadorDTO convertirEntrenadorADTO(Entrenador entrenador) {
        if (entrenador == null) return null;
        
        EntrenadorDTO dto = new EntrenadorDTO();
        dto.setNombre(entrenador.getNombre());

        List<PokemonDTO> pokemonesDTO = new ArrayList<>();
        for (Pokemon pokemon : entrenador.getPokemones()) {
            pokemonesDTO.add(convertirPokemonADTO(pokemon));
        }
        dto.setPokemones(pokemonesDTO);

        List<ItemDTO> itemsDTO = new ArrayList<>();
        for (Item item : entrenador.getItems()) {
            itemsDTO.add(convertirItemADTO(item));
        }
        dto.setItems(itemsDTO);
        
        return dto;
    }

    /**
     * Convierte un Pokemon de dominio a PokemonDTO
     */
    private PokemonDTO convertirPokemonADTO(Pokemon pokemon) {
        if (pokemon == null) return null;
        
        PokemonDTO dto = new PokemonDTO();
        dto.setNombre(pokemon.getName());
        dto.setPsActual(pokemon.getPs());
        dto.setPsMaximo(pokemon.getMaxPs());
        dto.setEstado(pokemon.getStatus());

        if (pokemon.getTipo1() != null) {
            dto.setTipo1(pokemon.getTipo1().getNombre());
        }
        
        if (pokemon.getTipo2() != null) {
            dto.setTipo2(pokemon.getTipo2().getNombre());
        }
        
        dto.setAttack(pokemon.getAttack());
        dto.setDefense(pokemon.getDefense());

        List<MovimientoDTO> movimientosDTO = new ArrayList<>();
        if (pokemon.getMovimientos() != null) {
            for (Movimiento movimiento : pokemon.getMovimientos()) {
                movimientosDTO.add(convertirMovimientoADTO(movimiento));
            }
        }
        dto.setMovimientos(movimientosDTO);
        
        return dto;
    }

    /**
     * Convierte un Movimiento de dominio a MovimientoDTO
     */
    private MovimientoDTO convertirMovimientoADTO(Movimiento movimiento) {
        if (movimiento == null) return null;
        
        MovimientoDTO dto;

        if (movimiento instanceof MovimientoFisico) {
            dto = new MovimientoFisicoDTO();
        } else if (movimiento instanceof MovimientoEspecial) {
            dto = new MovimientoEspecialDTO();
        } else if (movimiento instanceof MovimientoEstado) {
            dto = new MovimientoEstadoDTO();
        } else {
            dto = new MovimientoDTO();
        }
 
        dto.setNombre(movimiento.getNombre());
        dto.setTipo(movimiento.getTipo().getNombre());
        dto.setPoder(movimiento.getPoder());
        dto.setPrecision(movimiento.getPrecision());
        dto.setPPActual(movimiento.getPP());
        dto.setPPMaximo(movimiento.getMaxPP());
        
        return dto;
    }

    /**
     * Convierte un Item de dominio a ItemDTO
     */
    private ItemDTO convertirItemADTO(Item item) {
        if (item == null) return null;
        
        ItemDTO dto;

        if (item instanceof NormalPotion) {
            dto = new PotionDTO();
            ((PotionDTO) dto).setCantidadCuracion(((NormalPotion) item).getCantidadCuracion());
            dto.setTipo("NormalPotion");
        } else if (item instanceof SuperPotion) {
            dto = new PotionDTO();
            ((PotionDTO) dto).setCantidadCuracion(((SuperPotion) item).getCantidadCuracion());
            dto.setTipo("SuperPotion");
        } else if (item instanceof HyperPotion) {
            dto = new PotionDTO(); 
            ((PotionDTO) dto).setCantidadCuracion(((HyperPotion) item).getCantidadCuracion());
            dto.setTipo("HyperPotion");
        } else if (item instanceof Revive) {
            dto = new ReviveDTO();
            dto.setTipo("Revive");
        } else {
            dto = new ItemDTO();
            dto.setTipo("Unknown");
        }

        dto.setNombre(item.getNombre());
        dto.setDescripcion(item.getDescripcion());
        
        return dto;
    }
    
    /**
     * Obtiene información del entrenador 1 como DTO
     */
    public EntrenadorDTO getEntrenador1DTO() {
        return convertirEntrenadorADTO(MainGame.getEntrenador1());
    }

    /**
     * Obtiene información del entrenador 2 como DTO
     */
    public EntrenadorDTO getEntrenador2DTO() {
        return convertirEntrenadorADTO(MainGame.getEntrenador2());
    }
    
    /**
     * Obtiene el entrenador 1 configurado
     */
    public Entrenador getEntrenador1() {
        return entrenador1Configurado;
    }
    
    /**
     * Obtiene el entrenador 2 configurado
     */
    public Entrenador getEntrenador2() {
        return entrenador2Configurado;
    }
    
    /**
     * Obtiene la lista de pokémon disponibles como DTOs
     */
    public List<PokemonDTO> getPokemonesDisponiblesDTO() {
        List<PokemonDTO> pokemonesDTO = new ArrayList<>();
        ArrayList<Pokemon> pokemonesDisponibles = MainGame.getPokemonesDisponibles();
        
        for (Pokemon pokemon : pokemonesDisponibles) {
            pokemonesDTO.add(convertirPokemonADTO(pokemon));
        }
        
        return pokemonesDTO;
    }

    /**
     * Establece los pokémon seleccionados para un entrenador
     */
    public void setPokemonesSeleccionados(String nombreEntrenador, List<PokemonDTO> pokemonesSeleccionados) {
        if (entrenador1Configurado != null && entrenador1Configurado.getNombre().equals(nombreEntrenador)) {
            while (!entrenador1Configurado.getPokemones().isEmpty()) {
                entrenador1Configurado.eliminarPokemon(entrenador1Configurado.getPokemones().get(0));
            }

            for (PokemonDTO pokemonDTO : pokemonesSeleccionados) {
                for (Pokemon pokemon : MainGame.getPokemonesDisponibles()) {
                    if (pokemon.getName().equals(pokemonDTO.getName())) {
                        entrenador1Configurado.agregarPokemon(new Pokemon(pokemon));
                        break;
                    }
                }
            }
        } else if (entrenador2Configurado != null && entrenador2Configurado.getNombre().equals(nombreEntrenador)) {
            while (!entrenador2Configurado.getPokemones().isEmpty()) {
                entrenador2Configurado.eliminarPokemon(entrenador2Configurado.getPokemones().get(0));
            }
            
            for (PokemonDTO pokemonDTO : pokemonesSeleccionados) {
                for (Pokemon pokemon : MainGame.getPokemonesDisponibles()) {
                    if (pokemon.getName().equals(pokemonDTO.getName())) {
                        entrenador2Configurado.agregarPokemon(new Pokemon(pokemon));
                        break;
                    }
                }
            }
        }
    }

    /**
     * Establece los ítems seleccionados para un entrenador
     */
    public void setItemsSeleccionados(String nombreEntrenador, List<ItemDTO> itemsSeleccionados) {
        if (entrenador1Configurado != null && entrenador1Configurado.getNombre().equals(nombreEntrenador)) {
            while (!entrenador1Configurado.getItems().isEmpty()) {
                entrenador1Configurado.eliminarItem(entrenador1Configurado.getItems().get(0));
            }

            for (ItemDTO itemDTO : itemsSeleccionados) {
                Item item = crearItemDesdeDTO(itemDTO);
                if (item != null) {
                    entrenador1Configurado.agregarItem(item);
                }
            }
        } else if (entrenador2Configurado != null && entrenador2Configurado.getNombre().equals(nombreEntrenador)) {
            while (!entrenador2Configurado.getItems().isEmpty()) {
                entrenador2Configurado.eliminarItem(entrenador2Configurado.getItems().get(0));
            }
            
            for (ItemDTO itemDTO : itemsSeleccionados) {
                Item item = crearItemDesdeDTO(itemDTO);
                if (item != null) {
                    entrenador2Configurado.agregarItem(item);
                }
            }
        }
    }

    /**
     * Crea un objeto Item a partir de un ItemDTO
     */
    private Item crearItemDesdeDTO(ItemDTO itemDTO) {
        switch (itemDTO.getTipo()) {
            case "NormalPotion":
                return new NormalPotion();
            case "SuperPotion":
                return new SuperPotion();
            case "HyperPotion":
                return new HyperPotion();
            case "Revive":
                return new Revive();
            default:
                return null;
        }
    }

    /**
     * Muestra el panel de selección de Pokémon para el entrenador especificado
     * @param nombreEntrenador El nombre del entrenador que seleccionará sus Pokémon
     * @param maxSelections Número máximo de Pokémon a seleccionar
     */
    public void showPokemonSelection(String nombreEntrenador, int maxSelections) {
        pokemonSelectionPanel = new PokemonSelectionPanel(this, nombreEntrenador, maxSelections);

        Component[] components = cardPanel.getComponents();
        for (Component component : components) {
            if (component instanceof PokemonSelectionPanel) {
                cardPanel.remove(component);
                break;
            }
        }

        cardPanel.add(pokemonSelectionPanel, "pokemonSelection");
        cardLayout.show(cardPanel, "pokemonSelection");

        cardPanel.revalidate();
        cardPanel.repaint();
        
    }

    /**
     * Muestra el panel de selección de ítems para el entrenador especificado
     * @param nombreEntrenador El nombre del entrenador que seleccionará sus ítems
     */
    public void showItemSelection(String nombreEntrenador) {
        itemSelectionPanel = new ItemSelectionPanel(this, nombreEntrenador);

        Component[] components = cardPanel.getComponents();
        for (Component component : components) {
            if (component instanceof ItemSelectionPanel) {
                cardPanel.remove(component);
                break;
            }
        }

        cardPanel.add(itemSelectionPanel, "itemSelection");
        cardLayout.show(cardPanel, "itemSelection");

        cardPanel.revalidate();
        cardPanel.repaint();
    }

    /**
     * Notifica que un entrenador ha completado su configuración
     */
    public void entrenadorListo(String nombreEntrenador) {
        if (entrenador1Configurado != null && entrenador1Configurado.getNombre().equals(nombreEntrenador)) {
            entrenador1Listo = true;
        } else if (entrenador2Configurado != null && entrenador2Configurado.getNombre().equals(nombreEntrenador)) {
            entrenador2Listo = true;
        }

        if (modalidadJuego == MODALIDAD_PVM && entrenador1Listo && !entrenador2Listo) {
            if (entrenador2Configurado != null && entrenador2Configurado.getPokemones().isEmpty()) {
                ArrayList<Pokemon> pokemonesDisponibles = MainGame.getPokemonesDisponibles();
                MainGame.seleccionarEquipoAutomatico(entrenador2Configurado, pokemonesDisponibles, 6);
                MainGame.agregarItemsAutomatico(entrenador2Configurado);
            }
            entrenador2Listo = true;
        }

        if (entrenador1Listo && entrenador2Listo) {
            iniciarBatalla();
        } else if (modalidadJuego == MODALIDAD_PVP) {
            if (entrenador1Listo && !entrenador2Listo) {
                showPokemonSelection(entrenador2Configurado.getNombre(), 6);
            } else if (!entrenador1Listo && entrenador2Listo) {
                showPokemonSelection(entrenador1Configurado.getNombre(), 6);
            }
        } else {
            iniciarBatalla();
        }
    }
    
    private void inicializarDatos() {
        try {
            datosInicializados = MainGame.iniciarJuegoGUI(MODO_NORMAL);
            
            if (!datosInicializados) {
                System.out.println("Advertencia: No se pudieron cargar los datos iniciales. Algunas funciones pueden no estar disponibles.");
            }
        } catch (Exception e) {
            System.err.println("Error al inicializar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Guarda el estado actual de la partida
     */
    public void guardarPartida() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Partida");

        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".save");
            }
            
            @Override
            public String getDescription() {
                return "Archivos de guardado de POOBkemon (*.save)";
            }
        });
        
        int result = fileChooser.showSaveDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            if (!selectedFile.getName().toLowerCase().endsWith(".save")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".save");
            }
            
            try {
                MainGame.setEntrenador1(entrenador1Configurado);
                MainGame.setEntrenador2(entrenador2Configurado);

                MainGame.guardarPartida(selectedFile, modoJuego, modalidadJuego);
                
                JOptionPane.showMessageDialog(
                    this,
                    "Partida guardada exitosamente en: " + selectedFile.getName(),
                    "Guardado Exitoso",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (POOBkemonPersistenceException e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar la partida: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        }
    }
    
    public void cargarPartida() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar Partida");

        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".save");
            }
            
            @Override
            public String getDescription() {
                return "Archivos de guardado de POOBkemon (*.save)";
            }
        });
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            try {
                MainGame.cargarPartida(selectedFile);

                entrenador1Configurado = MainGame.getEntrenador1();
                entrenador2Configurado = MainGame.getEntrenador2();

                modoJuego = MainGame.getModoJuego(); 
                modalidadJuego = MainGame.getModalidadJuego();
                
                JOptionPane.showMessageDialog(
                    this,
                    "Partida cargada exitosamente desde: " + selectedFile.getName(),
                    "Carga Exitosa",
                    JOptionPane.INFORMATION_MESSAGE
                );

                if (MainGame.isBatallaEnCurso()) {
                    iniciarBatallaGuardada();
                } else {
                    showMainMenu();
                }
            } catch (POOBkemonPersistenceException e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar la partida: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        }
    }

    /**
     * Obtiene el modo de juego actual
     * @return el modo de juego (MODO_NORMAL o MODO_SUPERVIVENCIA)
     */
    public int getModoJuego() {
        return modoJuego;
    }
    
    /**
     * Establece el modo de juego
     * @param modo el nuevo modo de juego
     */
    public void setModoJuego(int modo) {
        this.modoJuego = modo;
    }
    
    /**
     * Obtiene la modalidad de juego actual
     * @return la modalidad de juego (MODALIDAD_PVP, MODALIDAD_PVM o MODALIDAD_MVM)
     */
    public int getModalidadJuego() {
        return modalidadJuego;
    }
    
    /**
     * Guarda el estado actual de la batalla
     * @param nombreE1 Nombre del entrenador 1
     * @param nombreE2 Nombre del entrenador 2
     * @param pokemon1Activo Pokémon activo del entrenador 1 (DTO)
     * @param pokemon2Activo Pokémon activo del entrenador 2 (DTO)
     * @param turnoE1 Indica si es el turno del entrenador 1
     */
    public void guardarEstadoBatalla(String nombreE1, String nombreE2, PokemonDTO pokemon1Activo, PokemonDTO pokemon2Activo, boolean turnoE1) {
        try {
            Pokemon pokemon1Dominio = encontrarPokemonEnDominio(entrenador1Configurado, pokemon1Activo.getName());
            Pokemon pokemon2Dominio = encontrarPokemonEnDominio(entrenador2Configurado, pokemon2Activo.getName());

            if (pokemon1Dominio != null) {
                pokemon1Dominio.setPs(pokemon1Activo.getPs());
                if (pokemon1Activo.getStatus() != null) {
                    pokemon1Dominio.setEstado(pokemon1Activo.getStatus());
                }
            }
            
            if (pokemon2Dominio != null) {
                pokemon2Dominio.setPs(pokemon2Activo.getPs());
                if (pokemon2Activo.getStatus() != null) {
                    pokemon2Dominio.setEstado(pokemon2Activo.getStatus());
                }
            }

            MainGame.setPokemon1Activo(pokemon1Dominio);
            MainGame.setPokemon2Activo(pokemon2Dominio);
            MainGame.setTurnoEntrenador1(turnoE1);
            MainGame.setBatallaEnCurso(true);
            MainGame.setModoJuego(modoJuego);
            MainGame.setModalidadJuego(modalidadJuego);

            guardarPartida();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al preparar el guardado de la batalla: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    /**
     * Encuentra un Pokémon en el dominio basado en su nombre
     * @param entrenador El entrenador donde buscar
     * @param nombrePokemon El nombre del Pokémon a buscar
     * @return El Pokémon de dominio encontrado, o null si no se encuentra
     */
    private Pokemon encontrarPokemonEnDominio(Entrenador entrenador, String nombrePokemon) {
        if (entrenador == null || entrenador.getPokemones() == null) {
            return null;
        }
        
        for (Pokemon pokemon : entrenador.getPokemones()) {
            if (pokemon.getName().equals(nombrePokemon)) {
                return pokemon;
            }
        }
        
        return null;
    }

    /**
     * Inicia una batalla con el estado recuperado de un guardado
     */
    private void iniciarBatallaGuardada() {
        try {
            battlePanel = new BattlePanel(this);

            Pokemon pokemon1Dominio = MainGame.getPokemon1Activo();
            Pokemon pokemon2Dominio = MainGame.getPokemon2Activo();
            
            PokemonDTO pokemon1DTO = pokemon1Dominio != null ? convertirPokemonADTO(pokemon1Dominio) : null;
            PokemonDTO pokemon2DTO = pokemon2Dominio != null ? convertirPokemonADTO(pokemon2Dominio) : null;

            battlePanel.restaurarEstadoGuardado(
                pokemon1DTO,
                pokemon2DTO,
                MainGame.isTurnoEntrenador1()
            );
            
            cardPanel.add(battlePanel, "battle");
            cardLayout.show(cardPanel, "battle");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al restaurar la batalla guardada: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
            showMainMenu();
        }
    }
    
    /**
     * Muestra el panel de selección de tipo de máquina
     */
    public void showMachineTypeSelection() {
        machineTypeSelectionPanel = new MachineTypeSelectionPanel(this, modalidadJuego);
        
        // Remover panel anterior si existe
        Component[] components = cardPanel.getComponents();
        for (Component component : components) {
            if (component instanceof MachineTypeSelectionPanel) {
                cardPanel.remove(component);
                break;
            }
        }
        
        cardPanel.add(machineTypeSelectionPanel, "machineTypeSelection");
        cardLayout.show(cardPanel, "machineTypeSelection");
        
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    /**
     * Muestra el panel de selección de tipo de máquina 2 (solo para MvM)
     */
    public void showMachineType2Selection() {
        machineType2SelectionPanel = new MachineType2SelectionPanel(this);

        Component[] components = cardPanel.getComponents();
        for (Component component : components) {
            if (component instanceof MachineType2SelectionPanel) {
                cardPanel.remove(component);
                break;
            }
        }
        
        cardPanel.add(machineType2SelectionPanel, "machineType2Selection");
        cardLayout.show(cardPanel, "machineType2Selection");
        
        cardPanel.revalidate();
        cardPanel.repaint();
    }
    
    /**
     * Establece el tipo de máquina para PvM
     */
    public void setTipoMaquina(String tipo) {
        MainGame.setTipoMaquinaSeleccionado(tipo);
    }

    /**
     * Establece el tipo de máquina 1 para MvM
     */
    public void setTipoMaquina1(String tipo) {
        this.tipoMaquina1 = tipo;
        MainGame.setTipoMaquina1Seleccionado(tipo);
    }

    /**
     * Establece el tipo de máquina 2 para MvM
     */
    public void setTipoMaquina2(String tipo) {
        this.tipoMaquina2 = tipo;
        MainGame.setTipoMaquina2Seleccionado(tipo);
    }

    /**
     * Inicia la batalla MvM con los tipos seleccionados
     */
    public void startMvMBattleWithTypes() {
        try {
            String cpu1Name = "CPU 1 (" + traductor(tipoMaquina1) + ")";
            String cpu2Name = "CPU 2 (" + traductor(tipoMaquina2) + ")";

            JOptionPane.showMessageDialog(
                this,
                "Iniciando batalla entre máquinas:\n" + cpu1Name + " vs " + cpu2Name,
                "Batalla Máquina vs Máquina",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            startBattle(cpu1Name, cpu2Name);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al iniciar la batalla MvM: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    /**
     * Traductor
     */
    private String traductor(String tipo) {
        switch (tipo) {
            case "attacking": return "Ofensivo";
            case "defensive": return "Defensivo";
            case "changing": return "Cambiante";
            case "expert": return "Experto";
            default: return "Desconocido";
        }
    }
    
    public static void main(String[] args) {
        PoobkemonGUI gui = new PoobkemonGUI();
        gui.setVisible(true);
    }
}
