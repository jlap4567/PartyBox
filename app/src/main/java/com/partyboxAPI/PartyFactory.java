package com.partyboxAPI;

import android.util.Log;

import com.google.common.collect.Sets;
import com.partyboxAPI.exceptions.PartyBoxException;
import com.partyboxAPI.exceptions.SavedPartiesDirectoryNotFoundException;

import java.io.File;
import java.util.Arrays;
import java.util.Set;

/**
 * Abstract Party Singleton Factory
 */
public abstract class PartyFactory {
    public static final String PARTY_FILE_PREFIX = ".party";
    private static final String PARTY_FILENAME_REGEX = "[0-9]{4}-[0-9]{2}-[0-9]{2}:*";
    private static Party currentParty = null;

    public static Party getNewOrCurrentParty() {
        if (currentParty == null) {
            currentParty = new Party();
        }

        return currentParty;
    }

    public static void clearCurrentParty() {
        currentParty = null;
    };

    public static Party initNewPartyFromJSON(JSONObjectWrapper jsonObjectWrapper) throws PartyBoxException {
        currentParty = new Party();
        currentParty.fromJSON(jsonObjectWrapper);
        return currentParty;
    }

    /**
     * Retrieves summary information about all saved parties without loading entire party into memory
     * The party file naming convention is
     * <code>
     *     yyyy-mm-dd:*.party
     * </code>
     * Expected local directory structure:
     * <code>
     *     app context directory {
     *         ...
     *         Parties directory {
     *             2018-10-23:Some Birthday Party.party
     *             2018-11-10:Booze Fest.party
     *             ...
     *         }
     *     }
     * </code>
     * @param path .../appContextDir/parties/
     * @return
     * @throws PartyBoxException
     */
    public static Set<PartySummary> getPartySummaries(String path) throws PartyBoxException {
        Set<PartySummary> partySummaries = Sets.newHashSet();
        File partiesDirectory = new File(path);
        if (!partiesDirectory.exists() || partiesDirectory.isFile()) {
            throw new SavedPartiesDirectoryNotFoundException(path);
        }

        File[] filesInPartyDir = partiesDirectory.listFiles();
        for (File file: filesInPartyDir) {
            partySummaries.add(getPartySummaryFromFilename(file.getName()));
            /*if (verifyPartyFile(file)) {
                try {
                    partySummaries.add(getPartySummaryFromFilename(file.getName()));
                } catch (PartyBoxException e) {
                    Log.e("Data", file.getName() + " has been corrupted.");
                    file.delete();
                }
            } else {
                Log.d("Misc", "Removing garbage in parties directory: " + file.getName());
                file.delete();
            }*/
        }

        return partySummaries;
    }

    private static boolean verifyPartyFile(File file) {
        return file.exists() && file.isFile() && file.getName().matches(PARTY_FILENAME_REGEX) && file.getName().endsWith(PARTY_FILE_PREFIX);
    }

    private static PartySummary getPartySummaryFromFilename(String fileName) throws PartyBoxException {
        String partyCode = fileName.substring(fileName.indexOf(PARTY_FILE_PREFIX));
        int seperationIndex = partyCode.indexOf(":");
        String[] partyCodeSplit = partyCode.split(":");
        // partyCodeSplit is guarentied to have length at least 2 since we chacked the party file name previously
        String partyDate = partyCodeSplit[0];
        partyCodeSplit[0] = "";
        String partyName = Arrays.toString(partyCodeSplit);
        try {
            return new PartySummary(partyDate, partyName);
        } catch (PartyBoxException e) {
            Log.e("Parse", e.getMessage());
            throw e;
        }
    }
}
