package domain;

import java.util.*;

public class ExpertTrainer extends EntrenadorMaquina {
    
    private Pokemon ultimoPokemonOponente;
    private Map<String, Integer> contadorUsoMovimientos;
    private int turnosEnBatalla;
    
    public ExpertTrainer(String nombre) {
        super(nombre, "expert");
        this.ultimoPokemonOponente = null;
        this.contadorUsoMovimientos = new HashMap<>();
        this.turnosEnBatalla = 0;
    }
    
    @Override
    protected Pokemon elegirMejorPokemon() {
        if (!tieneMasDeUnPokemonDisponible()) {
            return getPrimerPokemonDisponible();
        }
        if (ultimoPokemonOponente == null) {
            return elegirPokemonMasBalanceado();
        }
        return encontrarMejorMatchup(ultimoPokemonOponente);
    }
    
    /**
     * Encuentra el Pokémon con mejor balance general
     */
    private Pokemon elegirPokemonMasBalanceado() {
        Pokemon mejorPokemon = null;
        double mejorPuntaje = 0;
        
        for (Pokemon pokemon : getPokemones()) {
            if (!pokemon.estaDebilitado()) {
                double puntaje = calcularPuntajeGeneral(pokemon);
                
                if (mejorPokemon == null || puntaje > mejorPuntaje) {
                    mejorPokemon = pokemon;
                    mejorPuntaje = puntaje;
                }
            }
        }
        
        return mejorPokemon != null ? mejorPokemon : getPrimerPokemonDisponible();
    }
    
    /**
     * Calcula un puntaje general para un Pokémon
     */
    private double calcularPuntajeGeneral(Pokemon pokemon) {
        double puntaje = pokemon.getAttack() * 0.25 +
                        pokemon.getDefense() * 0.2 +
                        pokemon.getSpecialAttack() * 0.25 +
                        pokemon.getSpecialDefense() * 0.2 +
                        pokemon.getSpeed() * 0.1;
        
        double porcentajeSalud = (double) pokemon.getPs() / pokemon.getMaxPs();
        puntaje *= (0.5 + porcentajeSalud * 0.5);
        if (pokemon.tieneEstado()) {
            puntaje *= 0.7;
        }
        if (pokemonTieneMovimientosDisponibles(pokemon)) {
            puntaje *= 1.1;
        }
        return puntaje;
    }
    
    /**
     * Encuentra el mejor matchup contra un oponente específico
     */
    private Pokemon encontrarMejorMatchup(Pokemon oponente) {
        Pokemon mejorPokemon = null;
        double mejorPuntaje = 0;
        
        for (Pokemon pokemon : getPokemones()) {
            if (!pokemon.estaDebilitado()) {
                double puntaje = calcularPuntajeMatchup(pokemon, oponente);
                
                if (mejorPokemon == null || puntaje > mejorPuntaje) {
                    mejorPokemon = pokemon;
                    mejorPuntaje = puntaje;
                }
            }
        }
        
        return mejorPokemon != null ? mejorPokemon : getPrimerPokemonDisponible();
    }
    
    /**
     * Calcula qué tan bueno es un matchup (simplificado pero efectivo)
     */
    private double calcularPuntajeMatchup(Pokemon miPokemon, Pokemon oponente) {
        double puntaje = 0;
        double ventajaOfensiva = calcularVentajaTipoOfensiva(miPokemon, oponente);
        puntaje += ventajaOfensiva * 40;
        double resistenciaDefensiva = calcularResistenciaDefensiva(miPokemon, oponente);
        puntaje += resistenciaDefensiva * 30;
        if (miPokemon.getSpeed() > oponente.getSpeed()) {
            puntaje += 20;
        } else if (miPokemon.getSpeed() == oponente.getSpeed()) {
            puntaje += 10;
        }
        double porcentajeSalud = (double) miPokemon.getPs() / miPokemon.getMaxPs();
        puntaje += porcentajeSalud * 10;
        
        return puntaje;
    }
    
    /**
     * Calcula ventaja ofensiva de tipos
     */
    private double calcularVentajaTipoOfensiva(Pokemon atacante, Pokemon defensor) {
        double efectividad = 1.0;
        if (atacante.getTipo1() != null) {
            efectividad = Math.max(efectividad, 
                atacante.getTipo1().calcularEfectividadContraPokemon(defensor));
        }
        if (atacante.getTipo2() != null) {
            efectividad = Math.max(efectividad, 
                atacante.getTipo2().calcularEfectividadContraPokemon(defensor));
        }
        return efectividad;
    }
    
    /**
     * Calcula resistencia defensiva
     */
    private double calcularResistenciaDefensiva(Pokemon defensor, Pokemon atacante) {
        double efectividadContraMi = 1.0;
        
        if (atacante.getTipo1() != null) {
            efectividadContraMi = Math.max(efectividadContraMi, 
                atacante.getTipo1().calcularEfectividadContraPokemon(defensor));
        }
        
        if (atacante.getTipo2() != null) {
            efectividadContraMi = Math.max(efectividadContraMi, 
                atacante.getTipo2().calcularEfectividadContraPokemon(defensor));
        }
        return 2.0 - efectividadContraMi;
    }
    
    @Override
    protected Movimiento elegirMejorMovimiento(Pokemon pokemon) {
        turnosEnBatalla++;
        
        List<Movimiento> movimientos = pokemon.getMovimientos();
        if (movimientos == null || movimientos.isEmpty()) {
            return null;
        }
        List<Movimiento> movimientosDisponibles = filtrarMovimientosConPP(movimientos);
        if (movimientosDisponibles.isEmpty()) {
            return null;
        }
        
        return elegirMejorMovimiento(pokemon, movimientosDisponibles);
    }
    
    @Override
    protected Movimiento elegirMejorMovimiento(Pokemon pokemon, List<Movimiento> movimientosDisponibles) {
        EstrategiaMovimiento estrategia = determinarEstrategia(pokemon);
        
        switch (estrategia) {
            case CURACION:
                Movimiento curacion = buscarMovimientoCuracion(movimientosDisponibles);
                if (curacion != null) return curacion;
                break;
            case DEFENSIVA:
                Movimiento defensiva = buscarMovimientoDefensivo(movimientosDisponibles, pokemon);
                if (defensiva != null) return defensiva;
                break;
            case ESTADO:
                Movimiento estado = buscarMovimientoEstado(movimientosDisponibles);
                if (estado != null) return estado;
                break;
            case OFENSIVA:
            default:
                break;
        }
        return buscarMovimientoOfensivo(movimientosDisponibles, pokemon);
    }
    
    /**
     * Enum para las estrategias de movimiento
     */
    private enum EstrategiaMovimiento {
        CURACION, DEFENSIVA, ESTADO, OFENSIVA
    }
    
    /**
     * Determina la mejor estrategia según el contexto
     */
    private EstrategiaMovimiento determinarEstrategia(Pokemon pokemon) {
        double porcentajeSalud = (double) pokemon.getPs() / pokemon.getMaxPs();
        
        if (porcentajeSalud < 0.3) {
            return EstrategiaMovimiento.CURACION;
        }
        if (porcentajeSalud < 0.6 && turnosEnBatalla < 3) {
            return EstrategiaMovimiento.DEFENSIVA;
        }
        if (ultimoPokemonOponente != null && 
            !ultimoPokemonOponente.tieneEstado() && 
            ultimoPokemonOponente.getPs() > ultimoPokemonOponente.getMaxPs() * 0.7) {
            return EstrategiaMovimiento.ESTADO;
        }
        return EstrategiaMovimiento.OFENSIVA;
    }
    
    /**
     * Busca el mejor movimiento de curación
     */
    private Movimiento buscarMovimientoCuracion(List<Movimiento> movimientos) {
        for (Movimiento movimiento : movimientos) {
            if (movimiento instanceof MovimientoEstado && 
                movimiento.getNombre().equals("Respiro")) {
                return movimiento;
            }
        }
        return null;
    }
    
    /**
     * Busca el mejor movimiento defensivo
     */
    private Movimiento buscarMovimientoDefensivo(List<Movimiento> movimientos, Pokemon pokemon) {
        String[] movimientosDefensivos = {
            "Refugio", "Velo Aurora", "Corpulencia", "Eco Metalico"
        };
        
        for (String nombreDefensivo : movimientosDefensivos) {
            for (Movimiento movimiento : movimientos) {
                if (movimiento instanceof MovimientoEstado && 
                    movimiento.getNombre().equals(nombreDefensivo)) {
                    return movimiento;
                }
            }
        }
        return null;
    }
    
    /**
     * Busca el mejor movimiento de estado
     */
    private Movimiento buscarMovimientoEstado(List<Movimiento> movimientos) {
        String[] movimientosEstado = {
            "Tóxico", "Paralizador", "Hipnosis", "Fuego Fatuo", 
            "Onda Trueno", "Rayo Confuso"
        };
        
        for (String nombreEstado : movimientosEstado) {
            for (Movimiento movimiento : movimientos) {
                if (movimiento instanceof MovimientoEstado && 
                    movimiento.getNombre().equals(nombreEstado)) {
                    return movimiento;
                }
            }
        }
        return null;
    }
    
    /**
     * Busca el mejor movimiento ofensivo (optimizado)
     */
    private Movimiento buscarMovimientoOfensivo(List<Movimiento> movimientos, Pokemon pokemon) {
        Movimiento mejorMovimiento = null;
        double mejorPuntaje = 0;
        
        for (Movimiento movimiento : movimientos) {
            if (movimiento instanceof MovimientoFisico || 
                movimiento instanceof MovimientoEspecial) {
                
                double puntaje = calcularPuntajeOfensivo(movimiento, pokemon);
                
                if (mejorMovimiento == null || puntaje > mejorPuntaje) {
                    mejorMovimiento = movimiento;
                    mejorPuntaje = puntaje;
                }
            }
        }
        if (mejorMovimiento == null && !movimientos.isEmpty()) {
            return movimientos.get(random.nextInt(movimientos.size()));
        }
        
        return mejorMovimiento;
    }
    
    /**
     * Calcula puntaje ofensivo optimizado
     */
    private double calcularPuntajeOfensivo(Movimiento movimiento, Pokemon pokemon) {
        double puntaje = movimiento.getPoder();
        
        // STAB bonus
        if (movimiento.getTipo().equals(pokemon.getTipo1()) || 
            (pokemon.getTipo2() != null && movimiento.getTipo().equals(pokemon.getTipo2()))) {
            puntaje *= 1.5;
        }
        if (movimiento instanceof MovimientoFisico && 
            pokemon.getAttack() > pokemon.getSpecialAttack()) {
            puntaje *= 1.2;
        } else if (movimiento instanceof MovimientoEspecial && 
                   pokemon.getSpecialAttack() > pokemon.getAttack()) {
            puntaje *= 1.2;
        }
        int precision = movimiento.getPrecision();
        if (precision > 0) {
            puntaje *= (precision / 100.0);
        }
        String nombreMovimiento = movimiento.getNombre();
        Integer usos = contadorUsoMovimientos.get(nombreMovimiento);
        if (usos != null && usos > 2) {
            puntaje *= 0.8;
        }
        
        if (tieneEfectoSecundarioUtil(nombreMovimiento)) {
            puntaje *= 1.1;
        }
        
        return puntaje;
    }
    
    /**
     * Verifica si un movimiento tiene efectos secundarios útiles
     */
    private boolean tieneEfectoSecundarioUtil(String nombreMovimiento) {
        String[] movimientosConEfectos = {
            "Lanzallamas", "Rayo", "Rayo Hielo", "Triturar", 
            "Fuerza Lunar", "Bola Sombra", "Psíquico"
        };
        
        for (String nombre : movimientosConEfectos) {
            if (nombre.equals(nombreMovimiento)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public int elegirAccion(Entrenador oponente, Scanner scanner) throws POOBkemonException {
        Pokemon miPokemon = getPrimerPokemonDisponible();
        Pokemon pokemonOponente = oponente.getPrimerPokemonDisponible();
        
        ultimoPokemonOponente = pokemonOponente;
        if (necesitaCuracionUrgente(miPokemon) && !getItems().isEmpty()) {
            return 3;
        }
        if (deberiaCambiarPokemon(miPokemon, pokemonOponente)) {
            return 2;
        }
        return 1;
    }
    
    /**
     * Determina si necesita curación urgente
     */
    private boolean necesitaCuracionUrgente(Pokemon pokemon) {
        double porcentajeSalud = (double) pokemon.getPs() / pokemon.getMaxPs();
        if (porcentajeSalud < 0.3) {
            return true;
        }
        
        if (porcentajeSalud < 0.5 && pokemon.tieneEstado()) {
            String estado = pokemon.getStatus();
            return estado.equals("quemado") || estado.equals("envenenado") || 
                   estado.equals("gravemente envenenado");
        }
        return false;
    }
    
    /**
     * Determina si debería cambiar de Pokémon
     */
    private boolean deberiaCambiarPokemon(Pokemon miPokemon, Pokemon oponente) {
        if (!tieneMasDeUnPokemonDisponible()) {
            return false;
        }
        double porcentajeSalud = (double) miPokemon.getPs() / miPokemon.getMaxPs();
        if (porcentajeSalud < 0.25) {
            return tieneAlternativaMejor(miPokemon, oponente);
        }
        if (miPokemon.tieneEstado()) {
            String estado = miPokemon.getStatus();
            if (estado.equals("dormido") || estado.equals("congelado")) {
                return tieneAlternativaMejor(miPokemon, oponente);
            }
        }
        double puntajeActual = calcularPuntajeMatchup(miPokemon, oponente);
        if (puntajeActual < 40) {
            return tieneAlternativaMejor(miPokemon, oponente);
        }
        return false;
    }
    
    /**
     * Verifica si tenemos una alternativa significativamente mejor
     */
    private boolean tieneAlternativaMejor(Pokemon actual, Pokemon oponente) {
        double puntajeActual = calcularPuntajeMatchup(actual, oponente);
        
        for (Pokemon pokemon : getPokemones()) {
            if (!pokemon.equals(actual) && !pokemon.estaDebilitado()) {
                double puntajeAlternativo = calcularPuntajeMatchup(pokemon, oponente);
                
                if (puntajeAlternativo > puntajeActual * 1.3) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public Item elegirItem() throws POOBkemonException {
        List<Item> items = getItems();
        
        if (items.isEmpty()) {
            throw new POOBkemonException("No hay items disponibles.");
        }
        
        Pokemon pokemon = getPrimerPokemonDisponible();
        
        for (Pokemon p : getPokemones()) {
            if (p.estaDebilitado()) {
                for (Item item : items) {
                    if (item instanceof Revive) {
                        return item;
                    }
                }
                break;
            }
        }
        if (pokemon != null && !pokemon.estaDebilitado()) {
            int psRestantes = pokemon.getMaxPs() - pokemon.getPs();
            
            if (psRestantes >= 80) {
                for (Item item : items) {
                    if (item instanceof HyperPotion) return item;
                }
            }
            
            if (psRestantes >= 40) {
                for (Item item : items) {
                    if (item instanceof SuperPotion) return item;
                }
            }
            
            if (psRestantes >= 15) {
                for (Item item : items) {
                    if (item instanceof NormalPotion) return item;
                }
            }
        }
        
        return items.get(0);
    }
    
    @Override
    protected boolean deberiaUsarItem(Pokemon pokemon) {
        return necesitaCuracionUrgente(pokemon);
    }
}