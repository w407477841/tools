package com.xywg.hj212.common.enums;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:30 2019/7/5
 * Modified By : wangyifei
 */
public enum CommandEnum {

    COMMAND_ENUM_DATA("1062","实时数据"),
    COMMAND_ENUM_SYCNTIME("1013","同步时间"),
    ;

    CommandEnum(String commandCode, String commandName) {
        this.commandCode = commandCode;
        this.commandName = commandName;
    }

    private String commandCode;
    private String commandName;

    public String getCommandCode() {
        return commandCode;
    }

    public String getCommandName() {
        return commandName;
    }


    public static CommandEnum getEnum(String code){
        for(CommandEnum en:CommandEnum.values()){
            if(en.getCommandCode().equals(code)){
                return en;
            }
        }
        return null;
    }

}
