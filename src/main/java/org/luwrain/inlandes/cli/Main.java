/*
 * Copyright 2021-2024 Michael Pozhidaev <msp@luwrain.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.luwrain.inlandes.cli;

import java.io.*;

import org.luwrain.inlandes.*;
import static org.luwrain.inlandes.Token.*;

public final class  Main
{
    static public void main(String[] args )throws IOException
    {
	final Inlandes inlandes = new Inlandes();
	inlandes.loadStandardLibrary();
	parseArgs(inlandes, args);
	final BufferedReader r = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
	String line = r.readLine();
	while (line != null)
	{
	    System.out.println(concatText(inlandes.process(line)));
	    line = r.readLine();
	}
    }

    static private void parseArgs(Inlandes inlandes, String[] args) throws IOException
    {
	for(int i = 0;i < args.length;i++)
	{
	    final String a = args[i];
	    if (a.equals("--help") || a.equals("-h"))
		printHelp();
	    if (a.equals("-i") || a.equals("--input"))
	    {
		i++;
		if (i >=args.length)
		    errorMsg("--input(-i) requires an argument");
		System.setIn(new FileInputStream(args[i]));
		continue;
	    }
	    if (a.endsWith(".rules"))
		inlandes.loadRulesFromFile(a);
	}
    }

    static private void printHelp()
    {
	System.out.println(
			   "Inlandes: a text processing language" + System.lineSeparator() +
			   "Michael Pozhidaev <msp@luwrain.org>" + System.lineSeparator() +
			   "" + System.lineSeparator() +
			   "Usage: inlandes [OPTIONS] [file1.(rules|js) [file2.(rules|js)]]" + System.lineSeparator() +
			   "" + System.lineSeparator() +
			   "Options:" + System.lineSeparator() +
			   "--help, -h: print this help and exit" + System.lineSeparator() +
			   "--input FILE, -i FILE: read the input from FILE (stdin by default)" + System.lineSeparator()
			   );
	System.exit(0);
    }

    static private void  errorMsg(String text)
    {
	System.err.println("inlandes: " + text);
	System.exit(1);
    }
}
