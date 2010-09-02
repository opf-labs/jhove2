package org.jhove2.app.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math;



/**
 *
 * @author isaac32767
 */
public class ARules {

    private static enum Quantifier {
        ANY_OF,
        ALL_OF,
        NONE_OF
    }


    private static class Command {
        public String commandText;
        public int line;
    }


    private static class Rule {
        public String name;
        public Quantifier quantifier = null;
        public boolean enabled;
        public String description = "";
        public String consequent = "";
        public String alternative = "";
        public List<String> predicates = new ArrayList<String>();
    }

    private static class Ruleset {
        public String name;
        public boolean enabled;
        public String objectFilter;
        public String description = "";
        public List<Rule> rules = new ArrayList<Rule>();
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Pattern rulesetPattern =
                Pattern.compile("^ruleset\\s+(\\w+)" +
                    "\\s+(enabled|disabled)" +
                    "\\s+([:_A-Za-z][-.:_A-Za-z0-9]*)\\s*$");
        Pattern rulePattern =
                Pattern.compile("^rule\\s+(\\w+)" +
                    "\\s+(enabled|disabled)\\s*$");
        Pattern etcPattern =
                Pattern.compile("^(desc|cons|alt|quant|pred)\\s+(.*\\S)\\s*$");

        List<Ruleset> rulesets = new ArrayList<Ruleset>();





        for (String arg : args) {
            Ruleset currentRuleset = null;
            Rule currentRule = null;

            Command c;
            Matcher m;

            try {
                reader = new LineNumberReader(new FileReader(arg));
            } catch (FileNotFoundException ex) {
                System.err.println("Can't open file: " + ex.toString());
                System.exit(1);
            }
            try {
                while ((c = getCommand()) != null) {
                   m = rulesetPattern.matcher(c.commandText);
                   if (m.matches()) {
                      if (currentRuleset != null) {
                          System.err.printf("Second ruleset in %s line %d\n",
                                  arg, c.line);
                          System.exit(1);
                      }
                      rulesets.add(currentRuleset = new Ruleset());
                      currentRuleset.name = m.group(1);
                      currentRuleset.enabled = m.group(2).equals("enabled");
                      currentRuleset.objectFilter = m.group(3);
                      continue;
                   }

                   //If we got here, there better be a currentRuleset object
                   if (currentRuleset == null) {
                       System.err.println("Ruleset not first statement in " +
                               arg);
                       System.exit(1);
                   }

                   m = rulePattern.matcher(c.commandText);
                   if(m.matches()) {
                       if (currentRule != null) {
                           if (incompleteRule(currentRule)) {
                               System.err.printf("In %s, rule ends without all" +
                                       " values at line %d\n", arg, c.line);
                               System.exit(1);
                           }
                       }
                       currentRuleset.rules.add(currentRule = new Rule());
                       currentRule.name = m.group(1);
                       currentRule.enabled = m.group(2).equals("enabled");

                       continue;
                   }

                   m = etcPattern.matcher(c.commandText);
                   if (m.matches()) {
                       if (currentRule == null && !m.group(1).equals("desc")) {
                           System.err.printf("Missing rule statement in %s at " +
                                   "line %d\n", arg, c.line);
                           System.exit(1);
                       }
                       if (m.group(1).equals("desc")) {
                           if (currentRule == null) {
                               currentRuleset.description = m.group(2);
                           } else {
                               currentRule.description = m.group(2);
                           }
                       } else if (m.group(1).equals("cons")) {
                           currentRule.consequent = m.group(2);
                       } else if (m.group(1).equals("alt")) {
                           currentRule.alternative = m.group(2);
                       } else if (m.group(1).equals("quant")) {
                           if (m.group(2).equals("all")) {
                                currentRule.quantifier = Quantifier.ALL_OF;
                           } else if (m.group(2).equals("any")) {
                                currentRule.quantifier = Quantifier.ANY_OF;
                           } else {
                                currentRule.quantifier = Quantifier.NONE_OF;
                           }
                       } else { //pred
                           currentRule.predicates.add(m.group(2));
                       }
                       continue;
                   }
                   //Statement didn't match any patterns!
                   System.err.printf("Invalid statement in %s at line %d\n",
                           arg, c.line);
                }
                if (incompleteRule(currentRule)) {
                    System.err.println("Last rule in " + arg + " is incomplete");
                    System.exit(1);
                }
                if (incompleteRuleset(currentRuleset)) {
                    System.err.println("Ruleset in " + arg + " is incomplete");
                }
            } catch (IOException ex) {
                System.err.println("IO Error: " + ex.toString());
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        System.err.println("IO Error: " + ex.toString());
                    }
                }
            }
            
        } //for args
        
        String[] xmlPrefix = {
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
"<beans xmlns=\"http://www.springframework.org/schema/beans\"",
"       xmlns:util=\"http://www.springframework.org/schema/util\"",
"       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
"       xmlns:context=\"http://www.springframework.org/schema/context\"",
"       xsi:schemaLocation=\"http://www.springframework.org/schema/beans ",
"         http://www.springframework.org/schema/beans/spring-beans-2.5.xsd",
"         http://www.springframework.org/schema/context",
"         http://www.springframework.org/schema/context/spring-context-2.5.xsd",
"         http://www.springframework.org/schema/util",
"         http://www.springframework.org/schema/util/spring-util-2.0.xsd\">",
""            
        };
        
        for (String s : xmlPrefix) {
            System.out.println(s);
        }
        for (Ruleset rs: rulesets) {
            System.out.printf(
                    "    <bean id=\"%s\"" +
                    " class=\"org.jhove2.module.assess.RuleSet\"" +
                    " scope=\"singleton\">\n", rs.name);
            System.out.printf(
                    "        <property name=\"name\" value=\"%s\"/>\n",
                    rs.name);
            System.out.printf(
                    "        <property name=\"description\" value=\"%s\"/>\n",
                    rs.description);
            System.out.printf(
                    "        <property name=\"objectFilter\" value=\"%s\"/>\n",
                    rs.objectFilter);
            System.out.println(
                    "        <property name=\"rules\">");
            System.out.println(
                    "            <list" +
                    " value-type=\"org.jhove2.module.assess.Rule\">");

            for (Rule r: rs.rules) {
                System.out.printf(
                    "                <ref local=\"%s\"/>\n", r.name);
            }
            System.out.println(
                    "            </list>");
            System.out.println(
                    "        </property>");
            System.out.printf(
                    "        <property name=\"enabled\" value=\"%b\"/>\n",
                    rs.enabled);
            System.out.println(
                    "    </bean>");

            for (Rule r: rs.rules) {
                System.out.printf(
                        "    <bean id=\"%s\"" +
                        " class=\"org.jhove2.module.assess.Rule\"" +
                        " scope=\"singleton\">\n",
                        r.name);

                System.out.printf(
                        "      <property name=\"name\" value=\"%s\"/>\n",
                        r.name);
                System.out.printf(
                        "      <property name=\"description\" value=\"%s\"/>\n",
                        r.description);
                System.out.printf(
                        "      <property name=\"consequent\" value=\"%s\"/>\n",
                        r.consequent);
                System.out.printf(
                        "      <property name=\"alternative\" value=\"%s\"/>\n",
                        r.alternative);
                System.out.printf(
                        "      <property name=\"quantifier\" value=\"%s\"/>\n",
                        r.quantifier.name());
                System.out.println(
                        "        <property name=\"predicates\">");
                System.out.println(
                        "            <list value-type=\"java.lang.String\">");
                for (String s: r.predicates) {
                    System.out.printf(
                        "                <value><![CDATA[%s]]></value>\n",
                        s);
                }
                System.out.println(
                        "           </list></property>");
                System.out.printf(
                        "        <property name=\"enabled\" value=\"%b\"/>\n",
                    r.enabled);
                System.out.println(
                        "    </bean>");
            } //Rules
        } //Rulesets
        System.out.println("</beans>");
    }

    private static LineNumberReader reader;

    private static boolean incompleteRuleset(Ruleset r) {
        return r.description.isEmpty() || r.rules.isEmpty();
    }

    private static boolean incompleteRule(Rule r) {
        return r.description.isEmpty() || r.consequent.isEmpty() ||
                r.alternative.isEmpty() || r.predicates.isEmpty() ||
                r.quantifier == null;
    }


    



    private static Command getCommand() throws IOException {
        Command c = new Command();
        String continued;
       
       do {
            if ((c.commandText = reader.readLine()) == null) {
                return null;
            }
            //Trim right only in case first command is indented
            c.commandText = rightTrim(c.commandText);
       } while (c.commandText.isEmpty()); // Only empty before first command
       c.line = reader.getLineNumber();
       reader.mark(1000);
       //Suck up everything up to but not including next command or EOF
       while ((continued = reader.readLine()) != null &&
               (continued.isEmpty() ||
               continued.charAt(0) == ' ' ||
                continued.charAt(0) == '\t')) {
           c.commandText += " " + continued.trim();
           reader.mark(1000);
       }
       //Roll back following statement (if any)
       if (continued != null) {
            reader.reset();
       }

       return c;

    }

    private static String rightTrim(String s) {
        return ("x" + s).trim().substring(1);
    }

    //ARules is a utility class, so please don't instantiate it.
    private ARules() {
        throw new AssertionError();
    }

}

