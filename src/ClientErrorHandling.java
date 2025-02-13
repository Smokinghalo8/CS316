public class ClientErrorHandling {
    void CheckForCorrectAmountOfArguments(String[] args){
        if (args.length != 2){
            System.out.println("Please provide serverIP and serverPort");
        }
    }

    Boolean CheckForProperCommand(String command){
        return switch (command) {
            case "LIST", "UPLOAD", "DELETE", "RENAME", "DOWNLOAD" -> true;
            default -> false;
        };
    }


}
