package cn.com.vandesr.admin.service.impl;

import cn.com.vandesr.admin.entity.VandesrMenu;
import cn.com.vandesr.admin.entity.VandesrRoleMenu;
import cn.com.vandesr.admin.entity.VandesrUser;
import cn.com.vandesr.admin.entity.VandesrUserRole;
import cn.com.vandesr.admin.mapper.VandesrUserMapper;
import cn.com.vandesr.admin.service.*;
import cn.com.vandesr.admin.vo.MenuVo;
import cn.com.vandesr.backend.config.utils.SnowflakeIdWorker;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author code4fun
 * @since 2019-01-25
 */
@Slf4j
@Service
public class VandesrUserServiceImpl extends ServiceImpl<VandesrUserMapper, VandesrUser> implements IVandesrUserService {


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IVandesrUserRoleService userRoleService;
    @Autowired
    private IVandesrRoleMenuService roleMenuService;
    @Autowired
    private IVandesrMenuService menuService;

    SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);


    /**
     * 注册
     *
     * @param email
     * @param password
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(String userName, String email, String password) throws Exception {

        VandesrUser user = this.getUserByEmail(email);
        if (null != user) {
            throw new Exception("email账号已存在");
        }
        Date date = new Date();
        user = new VandesrUser();
        user.setCreateDate(date);
        user.setUpdateDate(date);
        user.setUserName(userName);
        user.setLoginName(email);
        password = passwordEncoder.encode(password);
        user.setPwd(password);

        user.setUserCode("U" + idWorker.nextId());
        user.setEmail(email);
        this.save(user);
    }

    /**
     * 判断邮箱是否唯一
     *
     * @param email
     * @return
     * @throws Exception
     */
    @Override
    public VandesrUser getUserByEmail(String email) throws Exception {
        QueryWrapper<VandesrUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email)
                .eq("delete_flag", 0);
        VandesrUser vandesrUser = this.getOne(queryWrapper);
        return vandesrUser;
    }


    /**
     * 通过userId获取用户角色信息
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public List<VandesrUserRole> getUserRolesByUserId(Integer userId) throws Exception {
        List<VandesrUserRole> userRoleList = new ArrayList<>();
        QueryWrapper<VandesrUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("delete_flag", 0);
        userRoleList = this.userRoleService.list(queryWrapper);
        return userRoleList;
    }

    /**
     * 通过角色信息获取菜单信息
     *
     * @param roleIdList
     * @return
     * @throws Exception
     */
    @Override
    public List<VandesrRoleMenu> getRoleMenuByRoleIds(List<Integer> roleIdList) throws Exception {
        List<VandesrRoleMenu> roleMenus = new ArrayList<>();
        QueryWrapper<VandesrRoleMenu> queryWrapper = new QueryWrapper<>();

        queryWrapper.in("role_id", roleIdList)
                .eq("delete_flag", 0);
        roleMenus = this.roleMenuService.list(queryWrapper);
        return roleMenus;
    }

    /**
     * 获取用户菜单信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<MenuVo> getUserMenuByUserId(Integer userId) throws Exception{
        boolean isContinue = true;
        List<VandesrUserRole> userRoleList = getUserRolesByUserId(userId);
        List<MenuVo> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(userRoleList)) {
            isContinue = false;
        }

        List<Integer> roleIdList = new ArrayList<>();
        List<VandesrRoleMenu> roleMenus = new ArrayList<>();
        if (isContinue) {
            userRoleList.forEach(userRole -> {
                roleIdList.add(userRole.getRoleId());
            });
            roleMenus = getRoleMenuByRoleIds(roleIdList);
        }

        List<VandesrMenu> menus = this.getMenu(roleMenus);
        if (CollectionUtils.isEmpty(menus)) {
            isContinue = false;
        }

        if (isContinue) {
            list = this.generateMenuTree(menus);
        }

        return list;
    }

    /**
     * 通过菜单信息构建界面上的菜单树信息
     * 1.将用户授权的所有菜单信息全部提取出来，然后再一级一级的找到上级菜单项信息
     * 2.
     * @param menus
     * @return
     * @throws Exception
     */
    private List<MenuVo> generateMenuTree(List<VandesrMenu> menus) throws Exception {
        List<MenuVo> list = new ArrayList<>();
        Map<String, List<MenuVo>> map = new HashMap<>(4);
        Set<String> parentIdSet = new HashSet<>();
        //父节点与当前节点映射关系
        Map<String, MenuVo> idMenuMap = new HashMap<>(4);

        for (VandesrMenu menu: menus) {
            Integer parentIdInt = menu.getParentId();
            MenuVo menuVo = menu2MenuVo(menu);

            if (null != parentIdInt) {

                String menuId = menu.getId() + "";
                String parentId = parentIdInt + "";
                idMenuMap.put(menuId, menuVo);
                if (!StringUtils.isEmpty(parentId)) {
                    parentIdSet.add(parentId);
                }

                VandesrMenu parentMenu = this.getMenuByMenuId(parentId);
                //一直向上找
                while (null != parentMenu.getParentId()) {
                    parentId = parentMenu.getParentId() + "";
                    if (!idMenuMap.containsKey(parentId)) {
                        parentMenu = this.getMenuByMenuId(parentId);
                        menuVo = menu2MenuVo(parentMenu);
                        idMenuMap.put(parentId, menuVo);
                    }
                    // map中没有父节点信息，从数据库中查找对应的父节点信息
                    parentMenu = this.getMenuByMenuId(parentId);

                }

            }
        }

        Iterator<String> iterator = parentIdSet.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (!idMenuMap.containsKey(next)) {
                VandesrMenu parentMenu = this.getMenuByMenuId(next);
                MenuVo parentMenuVo = menu2MenuVo(parentMenu);
                idMenuMap.put(next, parentMenuVo);

            }

        }



        if (null != idMenuMap && idMenuMap.size() > 0) {
            for (Map.Entry<String, MenuVo> entry : idMenuMap.entrySet()) {
                MenuVo node = entry.getValue();
                //MenuVo menuVo = menu2MenuVo(node);
                //一级菜单
                if (StringUtils.isEmpty(node.getParentId())) {
                    list.add(node);
                } else {
                    //不是一级菜单，构建菜子菜单
                    MenuVo parentMenu = idMenuMap.get(node.getParentId() + "");

                    if (null != parentMenu) {
                        idMenuMap.get(node.getParentId() + "").addChildrens(node);

                    }

                }

            }
        }

        return list;

    }

    /**
     * 将各个菜单关系组装成一个完整的关系链
     * 1-2-4
     * 1-3
     * 5-6-8
     * 5-7
     * ======
     * 1-2-4
     *   3
     * 5-6-8
     *   7
     * ======
     *
     *
     * @param parentIds
     * @return
     * @throws Exception
     */
    private Map<String, List<MenuVo>> generateMenuLinkByParentIds(List<String> parentIds) throws Exception{
        Map<String, List<MenuVo>> result = new ConcurrentHashMap<>(4);

        for (String parentId : parentIds) {
            //如果只有一个一级菜单，不显示
            if (parentId.indexOf(",") <= 0) {
                continue;
            }

            String[] menuIds = parentId.split(",");
            //获取一级菜单
            String rootMenuId = menuIds[0];
            //初始化
            for (int i = 1; i < menuIds.length; i++) {
                String menuId = menuIds[i];
                //获取menuId的菜单信息
                VandesrMenu menu = this.getMenuByMenuId(menuId);
                List<VandesrMenu> menuList = new ArrayList<>();
                menuList.add(menu);
                MenuVo menuVo = this.menu2MenuVo(menuList).get(0);
                //菜单树中已经存在一级菜单
                if (result.containsKey(rootMenuId)) {
                    result.get(rootMenuId).add(menuVo);

                } else {
                    List<MenuVo> list = new ArrayList<>();
                    list.add(menuVo);
                    result.put(rootMenuId, list);
                }

                break;
            }


        }


        return result;
    }

    private VandesrMenu getMenuByMenuId(String menuId) throws Exception {
        QueryWrapper<VandesrMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", menuId)
                .eq("delete_flag", 0);
        VandesrMenu menu = this.menuService.getOne(queryWrapper);

        return menu;
    }

    private MenuVo menu2MenuVo(VandesrMenu menu) throws Exception {
        MenuVo menuVo = new MenuVo();
        String parentId = menu.getParentId() == null ? null : menu.getParentId() + "";
        menuVo.setParentId(parentId);
        menuVo.setIcon(menu.getMenuIcon());
        menuVo.setTitle(menu.getMenuName());
        menuVo.setMenuId(menu.getId() + "");
        menuVo.setMenuCode(menu.getMenuCode());
        //路径
        menuVo.setPath(menu.getMenuUrl());
        return menuVo;
    }

    private List<MenuVo> menu2MenuVo(List<VandesrMenu> menus) throws Exception {
        List<MenuVo> menuVoList = new ArrayList<>();
        menus.forEach(menu -> {
            MenuVo menuVo = new MenuVo();
            menuVo.setIcon(menu.getMenuIcon());
            menuVo.setTitle(menu.getMenuName());
            menuVo.setMenuId(menu.getId() + "");
            menuVo.setMenuCode(menu.getMenuCode());
            //路径
            menuVo.setPath(menu.getMenuUrl());
            menuVoList.add(menuVo);
        });

        return menuVoList;
    }

    /**
     * 获取详细菜单信息
     * @param roleMenus
     * @return
     * @throws Exception
     */
    private List<VandesrMenu> getMenu(List<VandesrRoleMenu> roleMenus) throws Exception {
        List<VandesrMenu> menuList = new ArrayList<>();
        Set<Integer> menuIdSet = new HashSet<>();
        roleMenus.forEach(roleMenu -> {
            menuIdSet.add(roleMenu.getMenuId());
        });

        QueryWrapper<VandesrMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", menuIdSet)
                .eq("delete_flag", 0);

        log.info("-------------->");
        menuList = this.menuService.list(queryWrapper);
        log.info(queryWrapper.getSqlSelect());


        return menuList;
    }
}
