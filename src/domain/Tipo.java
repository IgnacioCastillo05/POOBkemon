package domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Tipo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nombre;
    private Map<String, Double> efectividades;
    
    private static final Map<String, Map<String, Double>> TABLA_EFECTIVIDADES = inicializarTablaEfectividades();
    
    public Tipo(String nombre) {
        this.nombre = nombre;
        this.efectividades = new HashMap<>(TABLA_EFECTIVIDADES.getOrDefault(nombre, new HashMap<>()));
    }
    
    private static Map<String, Map<String, Double>> inicializarTablaEfectividades() {
        Map<String, Map<String, Double>> tabla = new HashMap<>();
        
        // Normal
        Map<String, Double> normal = new HashMap<>();
        normal.put("Rock", 0.5);
        normal.put("Steel", 0.5);
        normal.put("Ghost", 0.0);
        tabla.put("Normal", normal);
        
        // Fire
        Map<String, Double> fire = new HashMap<>();
        fire.put("Fire", 0.5);
        fire.put("Water", 0.5);
        fire.put("Grass", 2.0);
        fire.put("Ice", 2.0);
        fire.put("Bug", 2.0);
        fire.put("Rock", 0.5);
        fire.put("Dragon", 0.5);
        fire.put("Steel", 2.0);
        tabla.put("Fire", fire);
        
        // Water
        Map<String, Double> water = new HashMap<>();
        water.put("Fire", 2.0);
        water.put("Water", 0.5);
        water.put("Grass", 0.5);
        water.put("Ground", 2.0);
        water.put("Rock", 2.0);
        water.put("Dragon", 0.5);
        tabla.put("Water", water);
        
        // Electric
        Map<String, Double> electric = new HashMap<>();
        electric.put("Water", 2.0);
        electric.put("Electric", 0.5);
        electric.put("Grass", 0.5);
        electric.put("Ground", 0.0);
        electric.put("Flying", 2.0);
        electric.put("Dragon", 0.5);
        tabla.put("Electric", electric);
        
        // Grass
        Map<String, Double> grass = new HashMap<>();
        grass.put("Fire", 0.5);
        grass.put("Water", 2.0);
        grass.put("Grass", 0.5);
        grass.put("Poison", 0.5);
        grass.put("Ground", 2.0);
        grass.put("Flying", 0.5);
        grass.put("Bug", 0.5);
        grass.put("Rock", 2.0);
        grass.put("Dragon", 0.5);
        grass.put("Steel", 0.5);
        tabla.put("Grass", grass);
        
        // Ice
        Map<String, Double> ice = new HashMap<>();
        ice.put("Fire", 0.5);
        ice.put("Water", 0.5);
        ice.put("Grass", 2.0);
        ice.put("Ice", 0.5);
        ice.put("Ground", 2.0);
        ice.put("Flying", 2.0);
        ice.put("Dragon", 2.0);
        ice.put("Steel", 0.5);
        tabla.put("Ice", ice);
        
        // Fighting
        Map<String, Double> fighting = new HashMap<>();
        fighting.put("Normal", 2.0);
        fighting.put("Ice", 2.0);
        fighting.put("Poison", 0.5);
        fighting.put("Flying", 0.5);
        fighting.put("Psychic", 0.5);
        fighting.put("Bug", 0.5);
        fighting.put("Rock", 2.0);
        fighting.put("Ghost", 0.0);
        fighting.put("Dark", 2.0);
        fighting.put("Steel", 2.0);
        fighting.put("Fairy", 0.5);
        tabla.put("Fighting", fighting);
        
        // Poison
        Map<String, Double> poison = new HashMap<>();
        poison.put("Grass", 2.0);
        poison.put("Poison", 0.5);
        poison.put("Ground", 0.5);
        poison.put("Rock", 0.5);
        poison.put("Ghost", 0.5);
        poison.put("Steel", 0.0);
        poison.put("Fairy", 2.0);
        tabla.put("Poison", poison);
        
        // Ground
        Map<String, Double> ground = new HashMap<>();
        ground.put("Fire", 2.0);
        ground.put("Electric", 2.0);
        ground.put("Grass", 0.5);
        ground.put("Poison", 2.0);
        ground.put("Flying", 0.0);
        ground.put("Bug", 0.5);
        ground.put("Rock", 2.0);
        ground.put("Steel", 2.0);
        tabla.put("Ground", ground);
        
        // Flying
        Map<String, Double> flying = new HashMap<>();
        flying.put("Electric", 0.5);
        flying.put("Grass", 2.0);
        flying.put("Fighting", 2.0);
        flying.put("Bug", 2.0);
        flying.put("Rock", 0.5);
        flying.put("Steel", 0.5);
        tabla.put("Flying", flying);
        
        // Psychic
        Map<String, Double> psychic = new HashMap<>();
        psychic.put("Fighting", 2.0);
        psychic.put("Poison", 2.0);
        psychic.put("Psychic", 0.5);
        psychic.put("Dark", 0.0);
        psychic.put("Steel", 0.5);
        tabla.put("Psychic", psychic);
        
        // Bug
        Map<String, Double> bug = new HashMap<>();
        bug.put("Fire", 0.5);
        bug.put("Grass", 2.0);
        bug.put("Fighting", 0.5);
        bug.put("Poison", 0.5);
        bug.put("Flying", 0.5);
        bug.put("Psychic", 2.0);
        bug.put("Ghost", 0.5);
        bug.put("Dark", 2.0);
        bug.put("Steel", 0.5);
        bug.put("Fairy", 0.5);
        tabla.put("Bug", bug);
        
        // Rock
        Map<String, Double> rock = new HashMap<>();
        rock.put("Fire", 2.0);
        rock.put("Ice", 2.0);
        rock.put("Fighting", 0.5);
        rock.put("Ground", 0.5);
        rock.put("Flying", 2.0);
        rock.put("Bug", 2.0);
        rock.put("Steel", 0.5);
        tabla.put("Rock", rock);
        
        // Ghost
        Map<String, Double> ghost = new HashMap<>();
        ghost.put("Normal", 0.0);
        ghost.put("Psychic", 2.0);
        ghost.put("Ghost", 2.0);
        ghost.put("Dark", 0.5);
        tabla.put("Ghost", ghost);
        
        // Dragon
        Map<String, Double> dragon = new HashMap<>();
        dragon.put("Dragon", 2.0);
        dragon.put("Steel", 0.5);
        dragon.put("Fairy", 0.0);
        tabla.put("Dragon", dragon);
        
        // Dark
        Map<String, Double> dark = new HashMap<>();
        dark.put("Fighting", 0.5);
        dark.put("Psychic", 2.0);
        dark.put("Ghost", 2.0);
        dark.put("Dark", 0.5);
        dark.put("Fairy", 0.5);
        tabla.put("Dark", dark);
        
        // Steel
        Map<String, Double> steel = new HashMap<>();
        steel.put("Fire", 0.5);
        steel.put("Water", 0.5);
        steel.put("Electric", 0.5);
        steel.put("Ice", 2.0);
        steel.put("Rock", 2.0);
        steel.put("Steel", 0.5);
        steel.put("Fairy", 2.0);
        tabla.put("Steel", steel);
        
        // Fairy
        Map<String, Double> fairy = new HashMap<>();
        fairy.put("Fire", 0.5);
        fairy.put("Fighting", 2.0);
        fairy.put("Poison", 0.5);
        fairy.put("Dragon", 2.0);
        fairy.put("Dark", 2.0);
        fairy.put("Steel", 0.5);
        tabla.put("Fairy", fairy);
        
        return tabla;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Calcula el factor de efectividad de este tipo contra el tipo defensor
     * @param tipoDefensor El tipo del Pokémon que recibe el ataque
     * @return Un factor de efectividad: 0.0 (inmune), 0.5 (no muy efectivo), 1.0 (normal), 2.0 (súper efectivo)
     */
    public double calcularEfectividad(Tipo tipoDefensor) {
        if (tipoDefensor == null) {
            return 1.0;
        }
        
        Double efectividad = efectividades.get(tipoDefensor.getNombre());
        return efectividad != null ? efectividad : 1.0;
    }
    
    /**
     * Calcula la efectividad contra un Pokémon que puede tener dos tipos
     * @param defensor El Pokémon que recibe el ataque
     * @return El factor de efectividad total (multiplicación de efectividades de ambos tipos)
     */
    public double calcularEfectividadContraPokemon(Pokemon defensor) {
        if (defensor == null) {
            return 1.0;
        }
        
        double efectividad = calcularEfectividad(defensor.getTipo1());
        
        if (defensor.getTipo2() != null) {
            efectividad *= calcularEfectividad(defensor.getTipo2());
        }
        
        return efectividad;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}