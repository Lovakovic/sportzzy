package ui;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import ui.except.CouldntLoadFileException;

import java.util.*;

public class CLI {
    private FIS fis;

    public CLI(String strPathToFcl) throws CouldntLoadFileException {
        this.fis = FIS.load(strPathToFcl);

        if(this.fis == null)
            throw new CouldntLoadFileException("The .fcl file specified in the path could not be loaded properly.");
    }

    /**
     * Converts a defuziffied Variable to a string tha matches it the best (calculated by its corresponding membership).
     * @param defuzzifiedVariable A Variable that is defined in DEFUZZIFY block in .fcl file.
     * @return Term from the corresponding block with the highest membership (term that best suits set Variables).
     */
    private String variableToSport(Variable defuzzifiedVariable) {
        Set<String> terms = defuzzifiedVariable.getLinguisticTerms().keySet();

        return terms.stream()
                .max(Comparator.comparingDouble(defuzzifiedVariable::getMembership))
                .orElse(null);
    }

    /**
     * Sets the FIS variables from given map.
     * @param userInput Map from which to set FIS variables.
     */
    private void setFisVariables(Map<String, Double> userInput) {
        for(String variable : userInput.keySet()) {
            this.fis.setVariable(variable, userInput.get(variable));
        }
    }

    /**
     * Just for testing, if you see this that must mean I'm either dead or I'm just dumb enough that I forgot to delete
     * it.
     */
    private Map<String, Double> usePredefined()  {
        Map<String, Double> variables = new HashMap<>();
        variables.put("current_fitness", 0.5);
        variables.put("impact_level", 0.);
        variables.put("number_of_teammates", 0.);
        variables.put("extremity_level", 0.6);
        variables.put("time_required", 0.);
        variables.put("season_dependence", 5.);
        variables.put("location_dependence", 5.);
        variables.put("budget_friendliness", 0.);

        return variables;
    }

    /**
     * Queries the user for each variable defined in .fcl file.
     * @return A map of all the queried variables with variable names as keys and the corresponding values as values.
     */
    private Map<String, Double> queryVariables() {

        Map<String, Double> variables = new HashMap<>();
        Scanner input = new Scanner(System.in);

        System.out.print("Your current fitness level (0 - poor, 10 - excellent): ");
        variables.put("current_fitness", input.nextDouble());

        System.out.print("Desired impact level of sport (0 - low, 10 - high): ");
        variables.put("impact_level", input.nextDouble());

        System.out.print("Desired number of teammates (0 - none, 10 - many): ");
        variables.put("number_of_teammates", input.nextDouble());

        System.out.print("Extremity level: (0 - not extreme, 10 - extreme): ");
        variables.put("extremity_level", input.nextDouble());

        System.out.print("Time required (0 - not much time required, 10 - lengthy sessions): ");
        variables.put("time_required", input.nextDouble());

        System.out.print("Sport season dependence (0 - completely independent, 10 - entirely dependant): ");
        variables.put("season_dependence", input.nextDouble());

        System.out.print("Sport location dependence (0 - completely independent, 10 - entirely dependant): ");
        variables.put("location_dependence", input.nextDouble());

        System.out.print("Budget friendliness (0 - cheap, 10 - expensive): ");
        variables.put("budget_friendliness", input.nextDouble());

        return variables;
    }

    /**
     * Starts the program.
     */
    public void run() {
        System.out.println("Welcome to the sport recommendation app!\nThis app will help you decide the perfect sport" +
                " according to your desires.");
        System.out.println("Please input the numbers within a specified range in accordance to desired sport " +
                "attributes.");

        Map<String, Double> userVariables = this.usePredefined();
        this.setFisVariables(userVariables);
        this.fis.evaluate();

        System.out.println(variableToSport(fis.getVariable("decision")));
    }

    /**
     * Displays the charts of fuzzified and defuzzified variables defined ini .fcl file.
     */
    public void showCharts(){
        JFuzzyChart.get().chart(this.fis.getFunctionBlock("sport"));
    }
}