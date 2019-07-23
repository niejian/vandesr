package cn.com.vandesr.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 * @author: nj
 * @date: 2019-07-19:15:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuRouterVo implements Serializable {
    private String path;
    private String title;
    private String component;

}
