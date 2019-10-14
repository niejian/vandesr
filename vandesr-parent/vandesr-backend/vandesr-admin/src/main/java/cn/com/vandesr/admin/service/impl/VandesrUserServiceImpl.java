package cn.com.vandesr.admin.service.impl;

import cn.com.vandesr.admin.entity.*;
import cn.com.vandesr.admin.mapper.VandesrUserMapper;
import cn.com.vandesr.admin.service.*;
import cn.com.vandesr.admin.vo.MenuRouterVo;
import cn.com.vandesr.admin.vo.MenuVo;
import cn.com.vandesr.backend.config.utils.SnowflakeIdWorker;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    @Autowired
    private IVandesrRoleService roleService;

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
     * 获取系统菜单树信息
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<MenuVo> getSystemMenuTree() throws Exception {
        // 获取所有角色信息
//        QueryWrapper<VandesrRole> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("delete_flag", 0);
//        List<VandesrRole> roleList = this.roleService.list(queryWrapper);
//        List<MenuVo> list = null;
//        if (!CollectionUtils.isEmpty(roleList)) {
//            List<Integer> roleIds = new ArrayList<>();
//            roleList.forEach(r -> {
//                roleIds.add(r.getId());
//            });
//            list = this.getMenuTreeByRoleIdList(roleIds);
//        }
        QueryWrapper<VandesrMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("delete_flag", 0);
        List<VandesrMenu> menus = this.menuService.list(queryWrapper);
        if (null == menus) {
            menus = new ArrayList<>();
        }

        List<MenuVo> list = this.getMenuTreeByMenus(menus);

        return list;


    }

    /**
     * 获取用户菜单树 getUserMenuByUserId 的优化版本
     * 1. 数据表：role_menu存储的是menuid是叶子节点。
     * 2.获取该用户的所有角色对应的所有叶子节点信息。
     * 3.通过叶子节点信息获取到其所有的父节点信息
     * 4. 从根节点开始往下寻找。判断在不在父节点列表。递归调用知
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public List<MenuVo> getMenuTreeByUserId(Integer userId) throws Exception {
        boolean isContinue = true;

        // 获取角色信息
        List<VandesrUserRole> userRoleList = getUserRolesByUserId(userId);
        List<MenuVo> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(userRoleList)) {
            isContinue = false;
        }

        // 根据角色信息获取对应的菜单信息
        List<Integer> roleIdList = new ArrayList<>();
        List<VandesrRoleMenu> roleMenus = new ArrayList<>();
        if (isContinue) {
            userRoleList.forEach(userRole -> {
                roleIdList.add(userRole.getRoleId());
            });

        }
        list = this.getMenuTreeByRoleIdList(roleIdList);
        return list;

    }

    private List<MenuVo> getMenuTreeByRoleIdList(List<Integer> roleIdList) throws Exception{
        boolean isContinue = true;
        List<VandesrRoleMenu> roleMenus = getRoleMenuByRoleIds(roleIdList);
        // 存放该用户下的所有菜单id
        List<MenuVo> list = new ArrayList<>();

        List<VandesrMenu> menus = null;
        if (isContinue && !CollectionUtils.isEmpty(roleMenus)) {
            menus = this.getMenu(roleMenus);
        }

        return getMenuTreeByMenus(menus);

    }

    /**
     * 通过菜单信息构造菜单树
     * 如果获取用户的菜单：先要查改用户的角色对应的菜单信息再调用该方法
     * 如果是获取整个系统的菜单树信息，那么就是查询所有有效的菜单信息再调用该方法
     * @date 2019年9月13日17:17:52
     * @param menus
     */
    private List<MenuVo> getMenuTreeByMenus(List<VandesrMenu> menus) throws Exception{
        List<MenuVo> list = new ArrayList<>();
        // 所有父节点id结合
        Set<String> parentIdSet = new HashSet<>();
        Set<Integer> uniqueMenuIdSet = new HashSet<>();

        if (null != menus) {
            for (VandesrMenu menu : menus) {
                uniqueMenuIdSet.add(menu.getId());
                String parentIds = menu.getParentIds();
                // 设置所有父节点信息
                if (!StringUtils.isEmpty(parentIds)) {
                    String[] parentIdArr = parentIds.split(",");
                    parentIdSet.addAll(Arrays.asList(parentIdArr));
                }
                if (!menu.isLeaf()) {
                    parentIdSet.add(menu.getId() + "");
                }
            }

            list = getMenuTree(uniqueMenuIdSet, parentIdSet);
        }

        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0).getChildren();
        } else {
            return list;
        }
    }

    /**
     * @param uniqueMenuIdSet 用户已经授权的所有菜单Id
     * @return
     */
    private List<MenuVo> getMenuTree(Set<Integer> uniqueMenuIdSet, Set<String> parentIdSet) throws Exception{
        List<MenuVo> menuTree = new ArrayList<>();
        // 1. 查找parent_id == null 的节点和非叶子节点 ，
        QueryWrapper<VandesrMenu> queryWrapper = new QueryWrapper<>();
        // 查到根节点
        queryWrapper.eq("delete_flag", 0)
                .isNull("parent_id");
        // 得到根节点，循环往下走
        List<VandesrMenu> list = this.menuService.list(queryWrapper);
        if (null == list) {
            return null;
        }

        for (VandesrMenu menu : list) {
            List<MenuVo> subMenus = getSubMenu(menu.getId(), uniqueMenuIdSet, parentIdSet);
            subMenus = subMenus == null ? new ArrayList<>() : subMenus;
            menuTree.addAll(subMenus);
        }

        return menuTree;

    }

    /**
     * 根据父节点id获取到子节点信息
     * @param parentId
     * @param uniqueMenuIdSet
     * @param parentIdSet 父节点结合
     * @return
     */
    private List<MenuVo> getSubMenu(Integer parentId, Set<Integer> uniqueMenuIdSet, Set<String> parentIdSet) throws Exception{
        List<MenuVo> menuTree = new ArrayList<>();
        VandesrMenu parentMenu = this.menuService.getById(parentId);

        //

        MenuVo parentMenuVo = menu2MenuVo(parentMenu);
        parentMenuVo.setHasChildren(true);

        QueryWrapper<VandesrMenu> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("delete_flag", 0)
                .eq("parent_id", parentId)
        .orderByAsc("id");
        List<VandesrMenu> list = this.menuService.list(queryWrapper);
        List<MenuVo> childrenList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(list)) {
            for (VandesrMenu menu : list) {

                Integer id = menu.getId();

                MenuVo menuVo = menu2MenuVo(menu);

                // 不是叶子节点, 并且改节点id在所有父节点集合中
                if (!menu.isLeaf() && parentIdSet.contains(id + "")) {
                    menuVo.setHasChildren(true);
                    List<MenuVo> subMenus = getSubMenu(id, uniqueMenuIdSet, parentIdSet);
                    if (!CollectionUtils.isEmpty(subMenus)) {
                        // subMenus已经包含了父节点的信息。所以这里只要获取它的children节点
                        menuVo.setChildren(subMenus.get(0).getChildren());
                    }
                    childrenList.add(menuVo);

                } else if (menu.isLeaf() && uniqueMenuIdSet.contains(id)){
                    // 找到叶子节点信息
                    menuVo.setHasChildren(false);
                    childrenList.add(menuVo);
                }

            }
            parentMenuVo.setChildren(childrenList);
        }
//        parentMenuVo.setChildren(childrenList);
        menuTree.add(parentMenuVo);

        return menuTree;
    }

    /**
     * 获取该用户的所有叶子信息（构建菜单路由）
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public List<MenuRouterVo> getLeafMenuByUserId(Integer userId) throws Exception {
        List<VandesrMenu> menus = this.getMenuByUserId(userId);
        List<MenuRouterVo> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(menus)) {
            for (VandesrMenu menu : menus) {
                if (menu.isLeaf()) {
                    MenuRouterVo vo = MenuRouterVo.builder()
                            .path(menu.getMenuUrl())
                            .title(menu.getMenuName())
                            .component(menu.getRouter())
                            .build();
                    list.add(vo);
                }

            }

        }
        return list;
    }

    /**
     * 获取指定用户的菜单信息
     * @param userId
     * @return
     * @throws Exception
     */
    private List<VandesrMenu> getMenuByUserId(Integer userId) throws Exception {
        List<VandesrMenu> userMenus = new ArrayList<>();
        boolean isContinue = true;
        List<VandesrUserRole> userRoleList = getUserRolesByUserId(userId);
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

        userMenus = this.getMenu(roleMenus);

        return userMenus;
    }

    /**
     * 获取用户菜单信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<MenuVo> getUserMenuByUserId(Integer userId) throws Exception{
        List<MenuVo> list = new ArrayList<>();
        List<VandesrMenu> menus = this.getMenuByUserId(userId);
        Boolean isContinue = true;
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
                        idMenuMap.get(node.getParentId() + "").setHasChildren(true);

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

        String path = menu.getMenuUrl();
        String routerPath = path;
        if (null != path && path.lastIndexOf("/") > 0) {
            int index = path.lastIndexOf("/");
            routerPath = path.substring(index);
        }
        String parentId = menu.getParentId() == null ? null : menu.getParentId() + "";
        MenuVo menuVo = (MenuVo.builder().parentId(parentId).icon(menu.getMenuIcon())
                .title(menu.getMenuName()).name(menu.getMenuName()).menuId(menu.getId() + "")
                .menuCode(menu.getMenuCode()).path(path)).routerPath(routerPath)
                .parentIds(menu.getParentIds()).build() ;

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
            MenuVo menuVo2 = MenuVo.builder().icon(menu.getMenuIcon())
                    .title(menu.getMenuName())
                    .menuId(menu.getId() + "")
                    .menuCode(menu.getMenuCode())
                    .parentIds(menu.getParentIds())
                    .parentId(menu.getParentId() + "")
                    .path(menu.getMenuUrl())
                    .build();
            menuVoList.add(menuVo2);
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

    /**
     * 分页获取用户信息
     *
     * @param userName
     * @param loginName
     * @param email
     * @param page
     * @return
     * @throws Exception
     */
    @Override
    public IPage<VandesrUser> getUsers(String userName, String loginName, String email, Page<VandesrUser> page) throws Exception {
        QueryWrapper<VandesrUser> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(userName)) {

            queryWrapper.like("user_name", userName);
        }

        if (!StringUtils.isEmpty(loginName)) {
            queryWrapper.like("login_name", loginName);
        }

        if (!StringUtils.isEmpty(email)) {
            queryWrapper.like("email", email);
        }


        return this.page(page, queryWrapper);
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeUser(String userId, String deleteFlag) {
        VandesrUser user = this.getById(userId);
        if (null != user) {
            user.setDeleteFlag(Integer.parseInt(deleteFlag));
            updateById(user);
        }
        return true;
    }
}
