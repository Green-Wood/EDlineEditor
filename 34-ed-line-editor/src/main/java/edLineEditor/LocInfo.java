package edLineEditor;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocInfo {
    private Page page;
    private int BeginIndex;
    private int EndIndex;
    private int toIndex;
    private char commandMark;
    private boolean isDefaultLoc;
    private String fileName;
    private String originStr;
    private String changeToStr;
    private int replaceCount;
    private char markChara;

    public LocInfo(String command, Page page) throws FalseInputFormatException{
        this.page = page;
        if (command.length() == 0 || command == null) throw new FalseInputFormatException();
        String originStr = command;
        check$AndStr(command);       // 检查是否含有 $-/str/
        command = dealReplace(command);    // 检查是否为替换,进行特殊处理
        if (Character.isAlphabetic(command.charAt(0))
                || command.charAt(0) == '=') {  // 检查是否为默认地址
            isDefaultLoc = true;
            BeginIndex = page.getCurrLine();
            EndIndex = page.getCurrLine();
            commandMark = command.charAt(0);
        }
        else if (command.charAt(0) == ';'){
            BeginIndex = page.getCurrLine();
            EndIndex = page.getSize() - 1;
            commandMark = command.charAt(1);
        }
        else if (command.charAt(0) == ','){
            BeginIndex = 0;
            EndIndex = page.getSize() - 1;
            try {
                commandMark = command.charAt(1);
            }catch (StringIndexOutOfBoundsException e){
                throw new FalseInputFormatException();
            }
        }
        else {
            while (command.contains("/")){              // 转化匹配字符
                int i1 = command.indexOf("/");
                int i2 = command.indexOf("/", i1 + 1);
                String s = command.substring(i1, i2 + 1);
                int lineNumber = page.findDownLineNumber(s.substring(1, s.length() - 1));
                command = command.replace(s, Integer.toString(lineNumber + 1));
            }
            while (command.contains("?")){
                int i1 = command.indexOf("?");
                int i2 = command.indexOf("?", i1 + 1);
                String s = command.substring(i1, i2 + 1);
                int lineNumber = page.findUpLineNumber(s.substring(1, s.length() - 1));
                command = command.replace(s, Integer.toString(lineNumber + 1));
            }
            while (command.contains("'")){                // 转化标记符
                int i = command.indexOf("'");
                String o = command.substring(i, i + 2);
                int lineNumber =page.getMark(o.charAt(1));
                command = command.replace(o, Integer.toString(lineNumber + 1));
            }
            if (command.contains(".")){                // 替换默认地址
                command = command.replace(".", Integer.toString(page.getCurrLine() + 1));
            }
            if (command.contains("$")){
                command = command.replace("$", Integer.toString(page.getSize()));
            }
            if (command.contains("-")) {
                command = dealMin(command);
            }
            if (command.contains("+")){
                command = dealPlus(command);
            }
            if (command.charAt(0) == ',' &&
                    Character.isDigit(command.charAt(command.indexOf(",") + 1))){ // 逗号右边有数字，左边没有
                int i;
                for (i = 0; i < command.length(); i++){
                    if (Character.isAlphabetic(command.charAt(i)) || command.charAt(i) == '=') break;
                }
                String old = command.substring(0, i);
                String New = Integer.toString(page.getCurrLine() + 1) + old;
                command = command.replace(old, New);
            }
            if (command.contains(",") &&
                    Character.isDigit(command.charAt(command.indexOf(",") - 1))
                    && (Character.isAlphabetic(command.charAt(command.indexOf(",") + 1))
                    || command.charAt(command.indexOf(",") + 1) == '=')){  // 逗号左边有数字，右边没有
                String old = command.substring(0, command.indexOf(",") + 1);
                String New = old + Integer.toString(page.getCurrLine() + 1);
                command = command.replace(old, New);
            }
            if (!command.contains(",")){
                int i;
                for (i = 0; i < command.length(); i++){
                    if (Character.isAlphabetic(command.charAt(i)) || command.charAt(i) == '='){
                        break;
                    }
                }
                String num = command.substring(0, i);
                String New = num + "," + num;
                command = New + command.substring(i, command.length());
            }

            int i;
            for (i = 0; i < command.length(); i++){
                if (Character.isAlphabetic(command.charAt(i)) || command.charAt(i) == '='){
                    break;
                }
            }
            command = command.substring(0, i) + " " + command.substring(i, command.length());

            String[] loc = command.split(" ")[0].split(",");
            BeginIndex = Integer.parseInt(loc[0]) - 1;
            EndIndex = Integer.parseInt(loc[1]) - 1;
            commandMark = command.split(" ")[1].charAt(0);
        }
        if (commandMark == 'm' || commandMark == 't'){
            dealMoveAndCopy(command, commandMark);
        }
        else if (commandMark == 'z'){
            if (originStr.charAt(originStr.length() - 1) == 'z'){
                toIndex = -1;
            }
            else {
                toIndex = Integer.parseInt(originStr.split("z")[1]);
            }
        }
        else if (commandMark == 'w' || commandMark == 'W' || commandMark == 'f'){
            if (originStr.split(" ").length == 2){
                fileName = originStr.split(" ")[1];
            }
            else fileName = "";
        }
        else if (commandMark == 'k'){
            int i = command.indexOf("k");
            markChara = command.charAt(i+1);
        }
        checkIsllegal();
    }

    private void checkIsllegal() throws FalseInputFormatException {
        if (BeginIndex < -1 || EndIndex < BeginIndex || EndIndex >= page.getSize()){
            throw new FalseInputFormatException();
        }
    }

    private static void check$AndStr(String command) throws FalseInputFormatException {
        Pattern p1 = Pattern.compile("\\$(\\+|-)/.+/");
        Pattern p2 = Pattern.compile("\\$(\\+|-)\\?.+\\?");
        Matcher m1 = p1.matcher(command);
        Matcher m2 = p2.matcher(command);
        if (m1.find() || m2.find())
            throw new FalseInputFormatException();
    }

    private void dealMoveAndCopy(String command, char commandMark) throws FalseInputFormatException {
        if (command.indexOf(commandMark) == command.length() - 1){
            toIndex = page.getCurrLine();
        }
        else {
            String to = command.substring(command.indexOf(commandMark) + 1, command.length());
            LocInfo toInfo = new LocInfo(to+"p", page);
            toIndex = toInfo.getBeginIndex();
        }
    }

    private String dealReplace(String command) throws FalseInputFormatException {
        Pattern p = Pattern.compile("s/.+/.+/(g|\\d*)");
        Matcher m = p.matcher(command);
        if (!m.find()) return command;
        String cutStr = m.group();
        String s = cutStr.substring(2, cutStr.length());
        originStr = s.split("/")[0];
        changeToStr = s.split("/")[1];
        if (s.split("/").length == 3){
            if (s.split("/")[2].equals("g")){
                replaceCount = -1;
            }
            else {
                try {
                    replaceCount = Integer.parseInt(s.split("/")[2]);
                }catch (Exception e){
                    throw new FalseInputFormatException();
                }
            }
        }
        else {
            replaceCount = 1;
        }
        int i = command.indexOf(cutStr);
        return command.substring(0, i + 1);
    }

    private String dealMin(String commandLine){
        int index = 0;
        for (int i = 0; i < commandLine.length(); i++){
            if (Character.isAlphabetic(commandLine.charAt(i)) || commandLine.charAt(i) == '=') break;
            index++;
        }
        String loc = commandLine.substring(0, index);

        if (loc.contains(",")){
            String s1 = loc.split(",")[0];
            String s2 = loc.split(",")[1];
            if (s1.contains("-")){
                int beginIndex;
                int endIndex;
                if (s1.indexOf("-") == 0) {
                    beginIndex = page.getCurrLine() + 1;
                }
                else {
                    beginIndex = Integer.parseInt(s1.split("-")[0]);
                }
                endIndex = Integer.parseInt(s1.split("-")[1]);
                s1 = Integer.toString(beginIndex - endIndex);
            }
            if (s2.contains("-")){
                int beginIndex;
                int endIndex;
                if (s2.indexOf("-") == 0) {
                    beginIndex = page.getCurrLine() + 1;
                }
                else {
                    beginIndex = Integer.parseInt(s2.split("-")[0]);
                }
                endIndex = Integer.parseInt(s2.split("-")[1]);
                s2 = Integer.toString(beginIndex - endIndex);
            }
            commandLine = s1 + "," + s2 + commandLine.substring(index, commandLine.length());
        }
        else {
            if (loc.contains("-")){
                int beginIndex;
                int endIndex;
                if (loc.indexOf("-") == 0) {
                    beginIndex = page.getCurrLine() + 1;
                }
                else {
                    beginIndex = Integer.parseInt(loc.split("-")[0]);
                }
                endIndex = Integer.parseInt(loc.split("-")[1]);
                loc = Integer.toString(beginIndex - endIndex);
            }
            commandLine = loc + commandLine.substring(index, commandLine.length());
        }
        return commandLine;
    }

    private String dealPlus(String commandLine){
        int index = 0;
        for (int i = 0; i < commandLine.length(); i++){
            if (Character.isAlphabetic(commandLine.charAt(i))) break;
            index++;
        }
        String loc = commandLine.substring(0, index);

        if (loc.contains(",")){
            String s1 = loc.split(",")[0];
            String s2 = loc.split(",")[1];
            if (s1.contains("+")){
                int beginIndex;
                int endIndex;
                if (s1.indexOf("+") == 0) {
                    beginIndex = page.getCurrLine() + 1;
                }
                else {
                    beginIndex = Integer.parseInt(s1.split("\\+")[0]);
                }
                endIndex = Integer.parseInt(s1.split("\\+")[1]);
                s1 = Integer.toString(beginIndex + endIndex);
            }
            if (s2.contains("+")){
                int beginIndex;
                int endIndex;
                if (s2.indexOf("+") == 0) {
                    beginIndex = page.getCurrLine() + 1;
                }
                else {
                    beginIndex = Integer.parseInt(s2.split("\\+")[0]);
                }
                endIndex = Integer.parseInt(s2.split("\\+")[1]);
                s2 = Integer.toString(beginIndex + endIndex);
            }
            commandLine = s1 + "," + s2 + commandLine.substring(index, commandLine.length());
        }
        else {
            if (loc.contains("+")){
                int beginIndex;
                int endIndex;
                if (loc.indexOf("+") == 0) {
                    beginIndex = page.getCurrLine() + 1;
                }
                else {
                    beginIndex = Integer.parseInt(loc.split("\\+")[0]);
                }
                endIndex = Integer.parseInt(loc.split("\\+")[1]);
                loc = Integer.toString(beginIndex + endIndex);
            }
            commandLine = loc + commandLine.substring(index, commandLine.length());
        }
        return commandLine;
    }


    public int getBeginIndex(){
        return this.BeginIndex;
    }

    public int getEndIndex(){
        return this.EndIndex;
    }

    public int getToIndex() {
        return this.toIndex;
    }

    public char getCommand() {
        return this.commandMark;
    }

    public boolean isDefaultLoc() {
        return isDefaultLoc;
    }

    public String fileName(){
        return fileName;
    }

    public String originStr(){
        return originStr;
    }

    public String changeToStr(){
        return changeToStr;
    }

    public int replaceCount() {
        return replaceCount;
    }

    public char markChara() {
        return markChara;
    }

    public static void main(String[] args){
        Pattern pattern = Pattern.compile("\\$(\\+|-)\\?.+\\?");
        Matcher matcher = pattern.matcher("$-?str?");
        if (matcher.find()){
            System.out.println(matcher.group());
        }
    }
}
