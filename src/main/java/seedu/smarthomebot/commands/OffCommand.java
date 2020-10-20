package seedu.smarthomebot.commands;

import seedu.smarthomebot.data.framework.Appliance;

import java.util.ArrayList;

import static java.util.stream.Collectors.toList;
import static seedu.smarthomebot.common.Messages.LINE;
import static seedu.smarthomebot.common.Messages.MESSAGE_APPLIANCE_NOT_EXIST;
import static seedu.smarthomebot.common.Messages.MESSAGE_APPLIANCE_PREVIOUSLY_OFF;

public class OffCommand extends Command {

    public static final String COMMAND_WORD = "off";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Turns off specified appliance by its indicated NAME \n"
            + "Parameters: NAME\n"
            + "Example: " + COMMAND_WORD + " Fan 1";
    private final String name;
    private static final String APPLIANCE_TYPE = "appliance";
    private static final String LOCATION_TYPE = "location";

    public OffCommand(String name) {
        this.name = name;
    }

    private int getApplianceToOffIndex() {
        for (Appliance appliance : applianceList.getAllAppliance()) {
            if (appliance.getName().equals((this.name))) {
                return applianceList.getAllAppliance().indexOf(appliance);
            }
        }
        return -1;
    }

    @Override
    public CommandResult execute() {
        String type = APPLIANCE_TYPE;
        ArrayList<Appliance> filterApplianceList =
                (ArrayList<Appliance>) applianceList.getAllAppliance().stream()
                        .filter((s) -> s.getLocation().equals(this.name))
                        .collect(toList());
        if (!filterApplianceList.isEmpty()) {
            type = LOCATION_TYPE;
        }
        switch (type) {
        case(APPLIANCE_TYPE) :
            int toOffApplianceIndex = getApplianceToOffIndex();
            if (toOffApplianceIndex < 0) {
                return new CommandResult(MESSAGE_APPLIANCE_NOT_EXIST);
            } else {
                Appliance toOffAppliance = applianceList.getAppliance(toOffApplianceIndex);
                String outputResult = displayOutput(toOffAppliance, "",0);
                return new CommandResult(outputResult);
            }
        case(LOCATION_TYPE) :
            if (locationList.isLocationCreated(this.name)) {
                String outputResults = LINE;
                for (Appliance toOffAppliance: applianceList.getAllAppliance()) {
                    if (toOffAppliance.getLocation().equals(this.name)) {
                        outputResults = displayOutput(toOffAppliance, outputResults, 1);
                    }
                }
                outputResults = outputResults.concat("All appliance in \"" + this.name + "\" are turned off ");
                return new CommandResult(outputResults);
            } else {
                return new CommandResult("No appliance in this location");
            }
        default :
            return new CommandResult("Invalid Format");
        }
    }

    private String displayOutput(Appliance toOffAppliance, String outputResults, int isList) {
        if (toOffAppliance.switchOff()) {
            assert toOffAppliance.getStatus().equals("OFF") : "Appliance should be already OFF";
            if (isList == 1) {
                outputResults = outputResults.concat(MESSAGE_APPLIANCE_PREVIOUSLY_OFF + "\n" + LINE);
            } else {
                outputResults = LINE + MESSAGE_APPLIANCE_PREVIOUSLY_OFF;
            }
        } else {
            assert toOffAppliance.getStatus().equals("OFF")  : "Appliance should be already OFF";
            if (isList == 1) {
                outputResults = outputResults.concat("Switching: " + toOffAppliance + "......OFF \n" + LINE);
            } else {
                outputResults = LINE + "Switching: " + toOffAppliance + "......OFF";
            }
        }
        return outputResults;
    }
}
