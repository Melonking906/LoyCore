package me.nonit.loycore.chat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;

public class StringWrapping
{
    /**
     * @param str The string to wrap
     * @param colourCode The colour code character to use, such as '&' or '§'
     * @param lineLength The line length to base around - not all lines will be this length exactly
     * @param wrapLongWords Whether long words should be cut up to fit the lines
     * @return The wrapped string, divided by new line characters
     */
    public static String wrapString(String str, char colourCode, int lineLength, boolean wrapLongWords)
    {
        // split up into words
        String[] split = WordUtils.wrap( str, lineLength, null, wrapLongWords ).split("\\r\\n");
        String[] fixed = new String[split.length];

        // set first element
        fixed[0] = split[0];

        for (int i = 1; i < split.length; i++)
        {
            String line = split[i];
            String previous = split[i - 1];

            // get last colour from last
            int code = previous.lastIndexOf(colourCode);

            // validate colour
            if (code != -1)
            {
                char cCode = previous.charAt(code == previous.length() - 1 ? code : code + 1);

                // colour has been split
                if (code == previous.length() - 1)
                {
                    // validate code
                    if (ChatColor.getByChar(line.charAt(0)) != null)
                    {
                        // remove off end of previous
                        fixed[i - 1] = previous.substring(0, previous.length() - 1);

                        // add § to start of line
                        line = "§" + line;
                        split[i] = line; // update for next iteration
                    }

                } else
                {
                    // check next line doesn't already have a colour
                    if (line.length() < 2 || line.charAt(0) != colourCode || ChatColor.getByChar(line.charAt(1)) == null)
                        // prepend line with colour
                        if ( ChatColor.getByChar( cCode ) != null)
                            line = "§" + cCode + line;
                }
            }

            // update the arrays
            fixed[i] = line;
            split[i] = line;

        }

        // join it all up to return a String
        return ChatColor.translateAlternateColorCodes(colourCode, StringUtils.join( fixed, '\n' ));
    }
}
