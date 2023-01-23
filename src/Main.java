import ui.CLI;

public class Main {
    public static void main(String[] args) {

        CLI cli = new CLI("fcl/fuzzyness.fcl");
        cli.run();
        cli.showAllCharts();
    }
}