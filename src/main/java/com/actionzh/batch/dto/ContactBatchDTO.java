package com.actionzh.batch.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemCountAware;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Data
@Slf4j
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class ContactBatchDTO extends ContactDTO implements ItemCountAware {

    private int currentItemCount;

    @Override
    public void setItemCount(int i) {
        this.currentItemCount = i;
    }

    private String attr1;
    private String attr2;
    private String attr3;
    private String attr4;
    private String attr5;
    private String attr6;
    private String attr7;
    private String attr8;
    private String attr9;
    private String attr10;
    private String attr11;
    private String attr12;
    private String attr13;
    private String attr14;
    private String attr15;
    private String attr16;
    private String attr17;
    private String attr18;
    private String attr19;
    private String attr20;
    private String attr21;
    private String attr22;
    private String attr23;
    private String attr24;
    private String attr25;
    private String attr26;
    private String attr27;
    private String attr28;
    private String attr29;
    private String attr30;
    private String attr31;
    private String attr32;
    private String attr33;
    private String attr34;
    private String attr35;
    private String attr36;
    private String attr37;
    private String attr38;
    private String attr39;
    private String attr40;
    private String attr41;
    private String attr42;
    private String attr43;
    private String attr44;
    private String attr45;
    private String attr46;
    private String attr47;
    private String attr48;
    private String attr49;
    private String attr50;
    private String attr51;
    private String attr52;
    private String attr53;
    private String attr54;
    private String attr55;
    private String attr56;
    private String attr57;
    private String attr58;
    private String attr59;
    private String attr60;
    private String attr61;
    private String attr62;
    private String attr63;
    private String attr64;

    private String source;
    private String campaign;
    private String medium;
    private String content;
    private String term;

    public String getAttrByName(String name) {
        String methodName = "get" + StringUtils.capitalize(name);
        Method getter = ReflectionUtils.findMethod(this.getClass(), methodName);
        String value = null;
        try {
            value = (String) ReflectionUtils.invokeMethod(getter, this);
        } catch (Exception ex) {
            log.error("Exception getPropertyByName!", ex);
        }
        return value;
    }

    public void setAttrByName(String name, String value) {
        String methodName = "set" + StringUtils.capitalize(name);
        Method setter = ReflectionUtils.findMethod(this.getClass(), methodName, String.class);
        try {
            ReflectionUtils.invokeMethod(setter, this, value);
        } catch (Exception ex) {
            log.error("Exception setPropertyByName!", ex);
        }
    }

}
