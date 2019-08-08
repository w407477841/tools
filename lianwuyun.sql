/*
 Navicat Premium Data Transfer

 Source Server         : 118.31.69.25
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : 118.31.69.25:3306
 Source Schema         : demon

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 08/08/2019 11:27:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence`  (
  `next_val` bigint(20) NULL DEFAULT NULL
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Fixed;

-- ----------------------------
-- Table structure for sys_areas
-- ----------------------------
DROP TABLE IF EXISTS `sys_areas`;
CREATE TABLE `sys_areas`  (
  `id` int(10) NOT NULL COMMENT 'ID',
  `areaname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '栏目名',
  `parentid` int(10) NOT NULL COMMENT '父栏目',
  `shortname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `areacode` int(6) NULL DEFAULT NULL,
  `zipcode` int(10) NULL DEFAULT NULL,
  `pinyin` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lng` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lat` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `level` tinyint(1) NOT NULL,
  `position` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sort` tinyint(3) UNSIGNED NULL DEFAULT 50 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'ID雪花算法',
  `simple_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '部门简称',
  `full_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '全称',
  `type` int(2) NULL DEFAULT NULL COMMENT '部门类型',
  `social_credit_number` varchar(18) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '社会信用代码',
  `super_admin` int(1) NULL DEFAULT NULL COMMENT '是否是超级管理员',
  `num` int(2) NULL DEFAULT NULL COMMENT '排序',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '部门' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1', '小王子', '小王子', 1, 'rqreq', 1, 0, 0, '2018-11-15 11:20:58', '1', '2018-11-15 11:20:58', '1');
INSERT INTO `sys_dept` VALUES ('1063419581007159297', '小大哥', '小大哥', 1, '12345678', 1, 0, 0, '2018-11-16 21:12:40', NULL, '2018-11-16 21:12:40', NULL);

-- ----------------------------
-- Table structure for sys_dept_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_relation`;
CREATE TABLE `sys_dept_relation`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'ID雪花算法',
  `parent_dept_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '父级部门ID',
  `child_dept_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '子级部门ID',
  `status` int(2) NULL DEFAULT NULL COMMENT '状态',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '更新人',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除标志 0：未删除，1：删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '部门父子关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept_relation
-- ----------------------------
INSERT INTO `sys_dept_relation` VALUES ('1', '1', '1', 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_dept_relation` VALUES ('1063419581137182721', '1', '1063419581007159297', 0, '2018-11-16 21:12:40', NULL, '2018-11-16 21:12:40', NULL, 0);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `group_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` int(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict_copy1
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_copy1`;
CREATE TABLE `sys_dict_copy1`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `group_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` int(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_group`;
CREATE TABLE `sys_dict_group`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID雪花算法',
  `code` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典组编码',
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典组名称',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `del_flag` int(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_group
-- ----------------------------
INSERT INTO `sys_dict_group` VALUES ('1063420682850820098', 'ooo', 'o', '', '2018-11-16 21:17:02', '', '2018-11-16 21:17:02', NULL);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '雪花算法ID',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除标志 0：未删除 1：删除',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `cost_time` int(11) NULL DEFAULT NULL COMMENT '消耗时间',
  `ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `ip_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip详细',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求名称',
  `request_param` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `request_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求类型',
  `request_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求地址',
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求者账号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `del_flag` int(1) NULL DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` int(11) NULL DEFAULT NULL,
  `sort_order` decimal(10, 2) NULL DEFAULT NULL,
  `component` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `level` int(5) NULL DEFAULT NULL,
  `button_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(1) NULL DEFAULT NULL,
  `is_show` int(1) NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('102580154190008320', '', '2019-01-29 15:49:16', 0, '', '2019-02-26 13:04:15', '', 'audit', '94595087891173376', 0, 4.00, 'audit/master/index', '/audit', '人工审核', 'audit', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('102580796434419712', NULL, '2019-01-29 15:51:49', 0, NULL, '2019-01-29 15:51:49', NULL, 'hidden', '94595087891173376', 0, 5.00, 'Main', '/hidden', '隐藏', 'airplane', 2, '', 0, 0);
INSERT INTO `sys_permission` VALUES ('102580957046902784', NULL, '2019-01-29 15:52:27', 0, NULL, '2019-01-29 15:52:27', NULL, 'hidden', '97882957288574976', 0, 2.00, 'Main', '/hidden', '隐藏', 'airplane', 2, '', 0, 0);
INSERT INTO `sys_permission` VALUES ('114075535830683648', '', '2019-03-02 09:07:48', 0, '', '2019-03-02 09:08:54', '', 'deviceLog', '94506304558600192', 0, 4.00, 'device/master/log/index', 'device/log', '日志管理', 'book', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('123150939530989568', '', '2019-03-27 10:10:13', 0, '', '2019-03-27 10:12:40', '', 'configfile', '94506304558600192', 0, 4.00, 'device/master/config-file/index', '/device/config-file', '配置文件', 'file', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('15701400130424832', '', '2018-11-15 11:20:58', 0, '', '2018-11-15 11:20:58', '', '', '5129710648430593', 1, 1.11, '', '/xboot/user/admin/add', '添加用户', '', 3, 'add', 0, 1);
INSERT INTO `sys_permission` VALUES ('15701915807518720', '', '2018-11-15 11:20:58', 0, '', '2018-11-15 11:20:58', '', '', '5129710648430593', 1, 1.13, '', '/xboot/user/admin/disable/**', '禁用用户', '', 3, 'disable', 0, 1);
INSERT INTO `sys_permission` VALUES ('15708892205944832', '', '2018-11-15 11:20:58', 0, '', '2018-11-15 11:20:58', '', '', '5129710648430593', 1, 1.14, '', '/xboot/user/admin/enable/**', '启用用户', '', 3, 'enable', 0, 1);
INSERT INTO `sys_permission` VALUES ('16392452747300864', '', '2018-11-15 11:20:58', 0, '', '2019-01-16 16:44:58', '', 'access', '', 0, 7.00, 'Main', '/access', '权限按钮测试页', 'locked', 1, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('16392767785668608', '', '2018-11-15 11:20:58', 0, '', '2018-11-15 11:20:58', '', 'access_index', '16392452747300864', 0, 2.10, 'access/access', 'index', '权限按钮测试页', 'locked', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('16438800255291392', '', '2018-11-15 11:20:58', 0, '', '2018-11-15 11:20:58', '', '', '16392767785668608', 1, 2.11, '', 'test-add', '添加按钮测试', '', 3, 'add', 0, 1);
INSERT INTO `sys_permission` VALUES ('16438962738434048', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', NULL, NULL, '16392767785668608', 1, 2.12, NULL, 'edit-test', '编辑按钮测试', NULL, 3, 'edit', 0, 1);
INSERT INTO `sys_permission` VALUES ('16439068543946752', '', '2018-11-15 11:20:58', 0, '', '2018-11-15 11:20:58', '', '', '16392767785668608', 1, 2.13, '', 'delete-test', '删除按钮测试', '', 3, 'delete', 0, 1);
INSERT INTO `sys_permission` VALUES ('16678126574637056', '', '2018-11-15 11:20:58', 0, '', '2018-11-15 11:20:58', '', '', '5129710648430593', 1, 1.12, '', '/xboot/user/admin/edit', '编辑用户', '', 3, 'edit', 0, 1);
INSERT INTO `sys_permission` VALUES ('16678447719911424', '', '2018-11-15 11:20:58', 0, '', '2018-11-15 11:20:58', '', '', '5129710648430593', 1, 1.15, '', '/xboot/user/delByIds**', '删除用户', '', 3, 'delete', 0, 1);
INSERT INTO `sys_permission` VALUES ('16687383932047360', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', NULL, NULL, '5129710648430594', 1, 1.21, NULL, '/xboot/role/save', '添加角色', NULL, 3, 'add', 0, 1);
INSERT INTO `sys_permission` VALUES ('16689632049631232', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', NULL, NULL, '5129710648430594', 1, 1.22, NULL, '/xboot/role/edit', '编辑角色', NULL, 3, 'edit', 0, 1);
INSERT INTO `sys_permission` VALUES ('16689745006432256', '', '2018-11-15 11:20:58', 0, '', '2018-11-15 11:20:58', '', '', '5129710648430594', 1, 1.23, '', '/xboot/role/delAllByIds**', '删除角色', '', 3, 'delete', 0, 1);
INSERT INTO `sys_permission` VALUES ('16689883993083904', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', NULL, NULL, '5129710648430594', 1, 1.24, NULL, '/xboot/role/editRolePerm/**', '分配权限', NULL, 3, 'editPerm', 0, 1);
INSERT INTO `sys_permission` VALUES ('16690313745666048', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', NULL, NULL, '5129710648430594', 1, 1.25, NULL, '/xboot/role/setDefault', '设为默认角色', NULL, 3, 'setDefault', 0, 1);
INSERT INTO `sys_permission` VALUES ('16694861252005888', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', NULL, NULL, '5129710648430595', 1, 1.31, NULL, '/xboot/permission/add', '添加菜单', NULL, 3, 'add', 0, 1);
INSERT INTO `sys_permission` VALUES ('16695107491205120', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', NULL, NULL, '5129710648430595', 1, 1.32, NULL, '/xboot/permission/edit', '编辑菜单', NULL, 3, 'edit', 0, 1);
INSERT INTO `sys_permission` VALUES ('16695243126607872', '', '2018-11-15 11:20:58', 0, '', '2018-11-15 11:20:58', '', '', '5129710648430595', 1, 1.33, '', '/xboot/permission/delByIds**', '删除菜单', '', 3, 'delete', 0, 1);
INSERT INTO `sys_permission` VALUES ('16695482789138432', '', '2018-11-15 11:20:58', 0, '', '2018-11-15 11:20:58', '', '', '5129710648430596', 1, 1.41, '', '/xboot/log/delByIds**', '删除日志', '', 3, 'delete', 0, 1);
INSERT INTO `sys_permission` VALUES ('16695638456537088', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', NULL, NULL, '5129710648430596', 1, 1.42, NULL, '/xboot/log/delAll', '清空日志', NULL, 3, 'clearAll', 0, 1);
INSERT INTO `sys_permission` VALUES ('25014528525733888', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', NULL, NULL, '5129710648430593', 1, 1.16, NULL, 'upload', '上传图片', NULL, 3, 'upload', 0, 1);
INSERT INTO `sys_permission` VALUES ('47153289405403136', '', '2018-11-15 11:20:58', 0, '', '2019-01-07 15:33:02', '', 'dictGroupManage', '5129710648430592', 0, 1.70, 'sys/dict-manage/dictGroupManage', 'dict-manage', '数据字典管理', 'dictGroupManage', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('47922655105388544', '', '2018-11-15 11:20:58', 0, '', '2019-01-07 15:33:08', '', 'deptManage', '5129710648430592', 0, 1.80, 'sys/dept-manage/deptManage', 'dept-manage', '部门管理', 'deptManage', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('5129710648430592', '', '2018-11-15 11:20:58', 0, '', '2019-01-24 14:55:46', '', 'sys', '', 0, 7.00, 'Main', '/form', '系统管理', 'ios-gear', 1, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('5129710648430593', '', '2018-11-15 11:20:58', 0, '', '2019-01-07 15:32:12', '', 'user-manage', '5129710648430592', 0, 1.10, 'sys/user-manage/userManage', 'user-manage', '用户管理', 'user-manage', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('5129710648430594', '', '2018-11-15 11:20:58', 0, '', '2019-01-07 15:32:22', '', 'role-manage', '5129710648430592', 0, 1.20, 'sys/role-manage/roleManage', 'role-manage', '角色管理', 'role-manage', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('5129710648430595', '', '2018-11-15 11:20:58', 0, '', '2019-01-07 15:32:28', '', 'menu-manage', '5129710648430592', 0, 1.30, 'sys/menu-manage/menuManage', 'menu-manage', '菜单权限管理', 'menu-manage', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('5129710648430596', '', '2018-11-15 11:20:58', 0, '', '2019-01-07 15:32:50', '', 'log-manage', '5129710648430592', 0, 1.40, 'sys/log-manage/logManage', 'log-manage', '日志管理', 'log-manage', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('94503602772512768', '', '2019-01-07 08:55:56', 0, '', '2019-01-07 16:05:06', '', 'dictionary', '', 0, 2.00, 'Main', '/dictionary', '数据字典', 'ios-pie', 1, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('94504972212768768', '', '2019-01-07 09:01:22', 0, '', '2019-01-07 15:31:54', '', 'type', '94503602772512768', 0, 3.10, 'dictionary/type/index', '/dictionary/type', '产品类型', 'type', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('94505414007197696', NULL, '2019-01-07 09:03:08', 0, NULL, '2019-01-07 09:03:08', NULL, 'operate', '94503602772512768', 0, 3.20, 'dictionary/operate/index', '/dictionary/operate', '操作系统', 'cog', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('94505902551339008', '', '2019-01-07 09:05:04', 0, '', '2019-01-07 16:06:29', '', 'upgrade', '', 0, 4.00, 'Main', '/upgrade', '设备服务', 'heart', 1, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('94506056801062912', NULL, '2019-01-07 09:05:41', 0, NULL, '2019-01-07 09:05:41', NULL, 'productfunction', '94503602772512768', 0, 3.30, 'dictionary/productfunction/index', '/dictionary/productfunction', '产品功能', 'construct', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('94506199331901440', '', '2019-01-07 09:06:15', 0, '', '2019-01-29 15:07:10', '', 'upgradeManager', '94505902551339008', 0, 2.00, 'device-service/upgrade/package', '/upgrade/Manager', '固件升级', 'book', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('94506264133898240', '', '2019-01-07 09:06:30', 0, '', '2019-01-29 15:07:17', '', 'remote', '94505902551339008', 0, 1.00, 'device-service/remote/config', '/remote', '远程配置', 'remote', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('94506304558600192', '', '2019-01-07 09:06:40', 0, '', '2019-01-07 15:20:30', '', 'device', '', 0, 3.00, 'Main', '/device', '设备管理', 'laptop', 1, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('94507280191459328', '', '2019-01-07 09:10:33', 0, '', '2019-01-10 15:19:12', '', 'manage', '94506304558600192', 0, 3.10, 'device/master/manage/index', '/device/manage', '设备管理', 'medical', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('94595087891173376', '', '2019-01-07 14:59:28', 0, '', '2019-01-07 15:59:53', '', 'account', '', 0, 1.00, 'Main', '/account', '注册信息', 'information-circled', 1, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('94596153923866624', '', '2019-01-07 15:03:42', 0, '', '2019-02-01 10:16:42', '', 'enterprise', '94595087891173376', 0, 2.00, 'registration/enterprise/enterprise', '/registration/enterprise', '企业认证', 'enterprise', 2, '', 0, 0);
INSERT INTO `sys_permission` VALUES ('94596331938516992', '', '2019-01-07 15:04:24', 0, '', '2019-02-01 10:30:17', '', 'authenticate', '94595087891173376', 0, 1.00, 'registration/authenticate/authenticate', '/registration/authenticate', '企业认证', 'none', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('94599623456329728', '', '2019-01-07 15:17:29', 0, '', '2019-01-28 13:52:48', '', 'product', '94506304558600192', 0, 3.00, 'device/master/product/index', '/product', '产品管理', 'product', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('94599788011458560', '', '2019-01-07 15:18:08', 0, '', '2019-01-08 15:53:44', '', 'networking', '94503602772512768', 0, 1.00, 'dictionary/link/linkType', '/networking', '联网方式', 'networking', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('94985879193391104', '', '2019-01-08 16:52:19', 0, '', '2019-01-16 16:46:57', '', 'runCondition', '94506304558600192', 0, 3.30, 'device/master/manage/runCondition', '/device/manage/runCondition', '运行数据', 'bicycle', 2, '', 0, 0);
INSERT INTO `sys_permission` VALUES ('97163463477956608', '', '2019-01-14 17:05:16', 0, '', '2019-01-15 17:14:54', '', 'result', '94595087891173376', 0, 3.00, 'registration/result/result', '/registration/result', '注册结果', 'result', 2, '', 0, 0);
INSERT INTO `sys_permission` VALUES ('97882957288574976', NULL, '2019-01-16 16:44:17', 0, NULL, '2019-01-16 16:44:17', NULL, 'app', NULL, 0, 5.00, 'Main', '/app', '应用管理', 'briefcase', 1, NULL, 0, 1);
INSERT INTO `sys_permission` VALUES ('97883542821801984', '', '2019-01-16 16:46:36', 0, '', '2019-02-26 13:03:53', '', 'appManage', '97882957288574976', 0, 5.10, 'app/master/manage/index', '/app/manage', '应用管理', 'app', 2, '', 0, 1);
INSERT INTO `sys_permission` VALUES ('98489830945591296', '', '2019-01-18 08:55:47', 0, '', '2019-01-29 15:04:40', '', 'debug', '94505902551339008', 0, 3.00, 'device-service/debug/debug', 'device-service/remote/debug', '在线调试', 'book', 2, '', 0, 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID雪花算法',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `del_flag` int(1) NULL DEFAULT NULL,
  `default_role` bit(1) NULL DEFAULT NULL COMMENT '是否是默认角色',
  `label` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('131493697706528768', NULL, '2019-04-19 10:41:22', NULL, '2019-04-19 10:41:22', 'ROLE_TEST', 0, NULL, NULL);
INSERT INTO `sys_role` VALUES ('496138616573952', '', '2018-11-15 11:20:58', '', '2018-11-15 11:20:58', 'ROLE_ADMIN', 0, NULL, NULL);
INSERT INTO `sys_role` VALUES ('95670623577051136', NULL, '2019-01-10 14:13:15', NULL, '2019-01-10 14:29:57', 'ROLE_BASE', 0, b'1', NULL);
INSERT INTO `sys_role` VALUES ('95670906298306560', NULL, '2019-01-10 14:14:23', NULL, '2019-01-10 14:14:23', 'ROLE_AUTH', 0, NULL, NULL);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID雪花算法',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `del_flag` int(1) NULL DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `permission_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单id',
  `role_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('101145551860404224', NULL, '2019-01-25 16:48:40', 0, NULL, '2019-01-25 16:48:40', '94595087891173376', '95670623577051136');
INSERT INTO `sys_role_permission` VALUES ('101145552225308672', NULL, '2019-01-25 16:48:40', 0, NULL, '2019-01-25 16:48:40', '94596331938516992', '95670623577051136');
INSERT INTO `sys_role_permission` VALUES ('101145552539881472', NULL, '2019-01-25 16:48:40', 0, NULL, '2019-01-25 16:48:40', '94596153923866624', '95670623577051136');
INSERT INTO `sys_role_permission` VALUES ('101145552850259968', NULL, '2019-01-25 16:48:40', 0, NULL, '2019-01-25 16:48:40', '97163463477956608', '95670623577051136');
INSERT INTO `sys_role_permission` VALUES ('114076016699248640', NULL, '2019-03-02 09:09:43', 0, NULL, '2019-03-02 09:09:43', '94595087891173376', '95670906298306560');
INSERT INTO `sys_role_permission` VALUES ('114076016997044224', NULL, '2019-03-02 09:09:43', 0, NULL, '2019-03-02 09:09:43', '94596331938516992', '95670906298306560');
INSERT INTO `sys_role_permission` VALUES ('114076017303228416', NULL, '2019-03-02 09:09:43', 0, NULL, '2019-03-02 09:09:43', '97163463477956608', '95670906298306560');
INSERT INTO `sys_role_permission` VALUES ('114076017613606912', NULL, '2019-03-02 09:09:43', 0, NULL, '2019-03-02 09:09:43', '94506304558600192', '95670906298306560');
INSERT INTO `sys_role_permission` VALUES ('114076018070786048', NULL, '2019-03-02 09:09:43', 0, NULL, '2019-03-02 09:09:43', '94599623456329728', '95670906298306560');
INSERT INTO `sys_role_permission` VALUES ('114076018397941760', NULL, '2019-03-02 09:09:43', 0, NULL, '2019-03-02 09:09:43', '94507280191459328', '95670906298306560');
INSERT INTO `sys_role_permission` VALUES ('114076018720903168', NULL, '2019-03-02 09:09:43', 0, NULL, '2019-03-02 09:09:43', '94985879193391104', '95670906298306560');
INSERT INTO `sys_role_permission` VALUES ('114076019073224704', NULL, '2019-03-02 09:09:43', 0, NULL, '2019-03-02 09:09:43', '114075535830683648', '95670906298306560');
INSERT INTO `sys_role_permission` VALUES ('114076019408769024', NULL, '2019-03-02 09:09:43', 0, NULL, '2019-03-02 09:09:43', '94505902551339008', '95670906298306560');
INSERT INTO `sys_role_permission` VALUES ('114076019744313344', NULL, '2019-03-02 09:09:44', 0, NULL, '2019-03-02 09:09:44', '94506264133898240', '95670906298306560');
INSERT INTO `sys_role_permission` VALUES ('114076020117606400', NULL, '2019-03-02 09:09:44', 0, NULL, '2019-03-02 09:09:44', '94506199331901440', '95670906298306560');
INSERT INTO `sys_role_permission` VALUES ('114076020444762112', NULL, '2019-03-02 09:09:44', 0, NULL, '2019-03-02 09:09:44', '98489830945591296', '95670906298306560');
INSERT INTO `sys_role_permission` VALUES ('114076020755140608', NULL, '2019-03-02 09:09:44', 0, NULL, '2019-03-02 09:09:44', '97882957288574976', '95670906298306560');
INSERT INTO `sys_role_permission` VALUES ('114076021090684928', NULL, '2019-03-02 09:09:44', 0, NULL, '2019-03-02 09:09:44', '97883542821801984', '95670906298306560');
INSERT INTO `sys_role_permission` VALUES ('123151046028562432', NULL, '2019-03-27 10:10:38', 0, NULL, '2019-03-27 10:10:38', '94595087891173376', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151046586404864', NULL, '2019-03-27 10:10:39', 0, NULL, '2019-03-27 10:10:39', '102580154190008320', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151047408488448', NULL, '2019-03-27 10:10:39', 0, NULL, '2019-03-27 10:10:39', '102580796434419712', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151047899222016', NULL, '2019-03-27 10:10:39', 0, NULL, '2019-03-27 10:10:39', '94503602772512768', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151048230572032', NULL, '2019-03-27 10:10:39', 0, NULL, '2019-03-27 10:10:39', '94599788011458560', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151048608059392', NULL, '2019-03-27 10:10:39', 0, NULL, '2019-03-27 10:10:39', '94504972212768768', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151048926826496', NULL, '2019-03-27 10:10:39', 0, NULL, '2019-03-27 10:10:39', '94505414007197696', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151049233010688', NULL, '2019-03-27 10:10:39', 0, NULL, '2019-03-27 10:10:39', '94506056801062912', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151049715355648', NULL, '2019-03-27 10:10:39', 0, NULL, '2019-03-27 10:10:39', '94506304558600192', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151050130591744', NULL, '2019-03-27 10:10:39', 0, NULL, '2019-03-27 10:10:39', '94599623456329728', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151050499690496', NULL, '2019-03-27 10:10:40', 0, NULL, '2019-03-27 10:10:40', '94507280191459328', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151050822651904', NULL, '2019-03-27 10:10:40', 0, NULL, '2019-03-27 10:10:40', '94985879193391104', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151051254665216', NULL, '2019-03-27 10:10:40', 0, NULL, '2019-03-27 10:10:40', '114075535830683648', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151051565043712', NULL, '2019-03-27 10:10:40', 0, NULL, '2019-03-27 10:10:40', '123150939530989568', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151052034805760', NULL, '2019-03-27 10:10:40', 0, NULL, '2019-03-27 10:10:40', '94505902551339008', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151052361961472', NULL, '2019-03-27 10:10:40', 0, NULL, '2019-03-27 10:10:40', '94506264133898240', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151052659757056', NULL, '2019-03-27 10:10:40', 0, NULL, '2019-03-27 10:10:40', '94506199331901440', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151053024661504', NULL, '2019-03-27 10:10:40', 0, NULL, '2019-03-27 10:10:40', '98489830945591296', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151053423120384', NULL, '2019-03-27 10:10:40', 0, NULL, '2019-03-27 10:10:40', '97882957288574976', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151053737693184', NULL, '2019-03-27 10:10:40', 0, NULL, '2019-03-27 10:10:40', '102580957046902784', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151054039683072', NULL, '2019-03-27 10:10:40', 0, NULL, '2019-03-27 10:10:40', '97883542821801984', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151054429753344', NULL, '2019-03-27 10:10:40', 0, NULL, '2019-03-27 10:10:40', '5129710648430592', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151054782074880', NULL, '2019-03-27 10:10:41', 0, NULL, '2019-03-27 10:10:41', '5129710648430593', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151055100841984', NULL, '2019-03-27 10:10:41', 0, NULL, '2019-03-27 10:10:41', '15701400130424832', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151055398637568', NULL, '2019-03-27 10:10:41', 0, NULL, '2019-03-27 10:10:41', '16678126574637056', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151055717404672', NULL, '2019-03-27 10:10:41', 0, NULL, '2019-03-27 10:10:41', '15701915807518720', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151056107474944', NULL, '2019-03-27 10:10:41', 0, NULL, '2019-03-27 10:10:41', '15708892205944832', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151056535293952', NULL, '2019-03-27 10:10:41', 0, NULL, '2019-03-27 10:10:41', '16678447719911424', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151056916975616', NULL, '2019-03-27 10:10:41', 0, NULL, '2019-03-27 10:10:41', '25014528525733888', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151057260908544', NULL, '2019-03-27 10:10:41', 0, NULL, '2019-03-27 10:10:41', '5129710648430594', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151057583869952', NULL, '2019-03-27 10:10:41', 0, NULL, '2019-03-27 10:10:41', '16687383932047360', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151057898442752', NULL, '2019-03-27 10:10:41', 0, NULL, '2019-03-27 10:10:41', '16689632049631232', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151058204626944', NULL, '2019-03-27 10:10:41', 0, NULL, '2019-03-27 10:10:41', '16689745006432256', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151058741497856', NULL, '2019-03-27 10:10:41', 0, NULL, '2019-03-27 10:10:41', '16689883993083904', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151059056070656', NULL, '2019-03-27 10:10:42', 0, NULL, '2019-03-27 10:10:42', '16690313745666048', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151059362254848', NULL, '2019-03-27 10:10:42', 0, NULL, '2019-03-27 10:10:42', '5129710648430595', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151059651661824', NULL, '2019-03-27 10:10:42', 0, NULL, '2019-03-27 10:10:42', '16694861252005888', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151060012371968', NULL, '2019-03-27 10:10:42', 0, NULL, '2019-03-27 10:10:42', '16695107491205120', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151060352110592', NULL, '2019-03-27 10:10:42', 0, NULL, '2019-03-27 10:10:42', '16695243126607872', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151060838649856', NULL, '2019-03-27 10:10:42', 0, NULL, '2019-03-27 10:10:42', '5129710648430596', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151061144834048', NULL, '2019-03-27 10:10:42', 0, NULL, '2019-03-27 10:10:42', '16695482789138432', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151061459406848', NULL, '2019-03-27 10:10:42', 0, NULL, '2019-03-27 10:10:42', '16695638456537088', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151061765591040', NULL, '2019-03-27 10:10:42', 0, NULL, '2019-03-27 10:10:42', '47153289405403136', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('123151062084358144', NULL, '2019-03-27 10:10:42', 0, NULL, '2019-03-27 10:10:42', '47922655105388544', '496138616573952');
INSERT INTO `sys_role_permission` VALUES ('131495839058104320', NULL, '2019-04-19 10:49:52', 0, NULL, '2019-04-19 10:49:52', '94595087891173376', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495839582392320', NULL, '2019-04-19 10:49:52', 0, NULL, '2019-04-19 10:49:52', '94596331938516992', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495839938908160', NULL, '2019-04-19 10:49:52', 0, NULL, '2019-04-19 10:49:52', '94596153923866624', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495840278646784', NULL, '2019-04-19 10:49:52', 0, NULL, '2019-04-19 10:49:52', '97163463477956608', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495840660328448', NULL, '2019-04-19 10:49:53', 0, NULL, '2019-04-19 10:49:53', '102580154190008320', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495841004261376', NULL, '2019-04-19 10:49:53', 0, NULL, '2019-04-19 10:49:53', '94503602772512768', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495841352388608', NULL, '2019-04-19 10:49:53', 0, NULL, '2019-04-19 10:49:53', '94599788011458560', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495841717293056', NULL, '2019-04-19 10:49:53', 0, NULL, '2019-04-19 10:49:53', '94504972212768768', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495842103169024', NULL, '2019-04-19 10:49:53', 0, NULL, '2019-04-19 10:49:53', '94505414007197696', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495842447101952', NULL, '2019-04-19 10:49:53', 0, NULL, '2019-04-19 10:49:53', '94506056801062912', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495842858143744', NULL, '2019-04-19 10:49:53', 0, NULL, '2019-04-19 10:49:53', '94506304558600192', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495843210465280', NULL, '2019-04-19 10:49:53', 0, NULL, '2019-04-19 10:49:53', '94599623456329728', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495843617312768', NULL, '2019-04-19 10:49:53', 0, NULL, '2019-04-19 10:49:53', '94507280191459328', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495843965440000', NULL, '2019-04-19 10:49:53', 0, NULL, '2019-04-19 10:49:53', '94985879193391104', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495844359704576', NULL, '2019-04-19 10:49:53', 0, NULL, '2019-04-19 10:49:53', '94505902551339008', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495844703637504', NULL, '2019-04-19 10:49:53', 0, NULL, '2019-04-19 10:49:53', '94506264133898240', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495845051764736', NULL, '2019-04-19 10:49:54', 0, NULL, '2019-04-19 10:49:54', '94506199331901440', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495845420863488', NULL, '2019-04-19 10:49:54', 0, NULL, '2019-04-19 10:49:54', '98489830945591296', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495845806739456', NULL, '2019-04-19 10:49:54', 0, NULL, '2019-04-19 10:49:54', '97882957288574976', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('131495846188421120', NULL, '2019-04-19 10:49:54', 0, NULL, '2019-04-19 10:49:54', '97883542821801984', '131493697706528768');
INSERT INTO `sys_role_permission` VALUES ('62329451395944448', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', '16392452747300864', '16457350655250432');
INSERT INTO `sys_role_permission` VALUES ('62329451425304576', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', '16392767785668608', '16457350655250432');
INSERT INTO `sys_role_permission` VALUES ('62329451458859008', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', '16439068543946752', '16457350655250432');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID雪花算法',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `avatar` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像路径',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件',
  `mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `nick_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `sex` int(1) NULL DEFAULT NULL COMMENT '性别',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态',
  `type` int(3) NULL DEFAULT NULL COMMENT '类型',
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除标志，0：未删除，1：删除',
  `dept_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('102242968890511360', NULL, '2019-01-28 17:29:25', NULL, '2019-01-28 17:29:25', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '19999999999', NULL, '$2a$10$9/QhRQtCdB1qYvAahJjKPuWiANYqRerLYABq7aYYQUYDTF2QwpQY6', NULL, 0, 0, 'sss', 0, NULL);
INSERT INTO `sys_user` VALUES ('102563712807735296', NULL, '2019-01-29 14:43:56', NULL, '2019-01-29 14:43:56', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '18761784776', NULL, '$2a$10$GZe1vGHJrAX23YxZ2Akv3.D3eUQMIRb.MiX1F2uUf3u9fli0R7gym', NULL, 0, 0, 'zzzz', 0, NULL);
INSERT INTO `sys_user` VALUES ('102586466906935296', NULL, '2019-01-29 16:14:21', NULL, '2019-01-29 16:14:21', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '15123352687', NULL, '$2a$10$WsjX8/WpmfiGvjNT/5XUzunX4sbr425PaJqVUtDOVFF7GQ8V608g.', NULL, 0, 0, 'hjy', 0, NULL);
INSERT INTO `sys_user` VALUES ('102602936734978048', NULL, '2019-01-29 17:19:48', NULL, '2019-01-29 17:19:48', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '18761784775', NULL, '$2a$10$NFPNwqWepLMngSY6d01N2eA8/ChxPfVTOZfzLxXJDH3Y3EgwQilki', NULL, 0, 0, 'xxxx', 0, NULL);
INSERT INTO `sys_user` VALUES ('102609512803864576', NULL, '2019-01-29 17:45:56', NULL, '2019-02-22 11:27:54', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '18662930720', NULL, '$2a$10$IxsAUPbRiHawgSaDOSUiDehDnScASnMoazWpCorQtCn3aeFHtyv7u', NULL, 0, 0, 'momo', 0, NULL);
INSERT INTO `sys_user` VALUES ('102614998114635776', NULL, '2019-01-29 18:07:43', NULL, '2019-02-18 09:42:09', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '18761784777', NULL, '$2a$10$avOlgkQQg/PnxBbyyzrh4OJOTnfsnSUkeJwmnb7K.jAgU3AWS100i', NULL, 0, 0, 'cmy', 0, NULL);
INSERT INTO `sys_user` VALUES ('102964571676872704', NULL, '2019-01-30 17:16:48', NULL, '2019-01-30 17:16:48', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '19951612810', NULL, '$2a$10$x9.LmG0AQ586ldIOAgOwXeLhtsVoZtM100ypLOm4Yk5QKCKD/HhIu', NULL, 0, 0, 'cccc', 0, NULL);
INSERT INTO `sys_user` VALUES ('103208037287727104', NULL, '2019-01-31 09:24:15', NULL, '2019-01-31 09:24:15', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '18252885368', NULL, '$2a$10$DtuBmVIsNKk/NItzkZK7T.59sudpBXKyw.SU8XC64AgTF8MH72rXe', NULL, 0, 0, 'jxj', 0, NULL);
INSERT INTO `sys_user` VALUES ('103568068130639872', '', '2019-02-01 09:14:53', '', '2019-02-12 09:32:40', '', 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', '', '1125654863@qq.com', '18761784774', '', '$2a$10$U4O935p2nNSVF59MDlXyJ.PbCElkqRj3ARHLlopYIPswh6nuJhKB.', NULL, 0, 0, 'aaazzz', 1, '');
INSERT INTO `sys_user` VALUES ('103574420412436480', '', '2019-02-01 09:40:08', '', '2019-02-12 09:33:02', '', 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', '', '1125654863@qq.com', '18761784775', '', '$2a$10$pqfnZLnrW8sp2heIaxz2P.gYr/2CvwdZe8N6GQMoW8EMb.0b7qRjm', 1, 0, 0, 'dddd', 0, '');
INSERT INTO `sys_user` VALUES ('108673485886197760', NULL, '2019-02-15 11:22:00', NULL, '2019-02-15 11:22:00', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '19951612820', NULL, '$2a$10$6vL7gRm4RN835tyu7quqIeqbezKqcWYc3xQVBwnIIm/xAvOuFcTKu', NULL, 0, 0, 'vvvv', 0, NULL);
INSERT INTO `sys_user` VALUES ('108753195064365056', NULL, '2019-02-15 16:38:44', NULL, '2019-02-15 16:38:44', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '18932224279', NULL, '$2a$10$tLU//5ndBC9GToAykYsHuerpquUNbAR2m5z/nO.TjtsuFcKHo/gDG', NULL, 0, 0, 'wangyifei', 0, NULL);
INSERT INTO `sys_user` VALUES ('110904503812755456', NULL, '2019-02-21 15:07:16', NULL, '2019-02-21 15:07:16', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '15366368207', NULL, '$2a$10$Y3qrddAO9w84J3BKdDUjt.dUwG8u62TcbiBFGiP9o1ncHPgj5xdGq', NULL, 0, 0, 'shilingyu', 0, NULL);
INSERT INTO `sys_user` VALUES ('113440042856222720', NULL, '2019-02-28 15:02:35', NULL, '2019-03-05 13:26:21', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '16605133973', NULL, '$2a$10$.mS8UBQCSakQqe0.P/ukTO56xt.w9zicB5k2T7co7yvg.uU09SblG', NULL, 0, 0, 'wo', 0, NULL);
INSERT INTO `sys_user` VALUES ('114224742218600448', NULL, '2019-03-02 19:00:42', NULL, '2019-03-02 19:00:42', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '15123352688', NULL, '$2a$10$YN1Z9Yi2wpjaMJy84HXLMeIiZYRx.bPAAq99R2G8U7Z5jXIy6NWse', NULL, 0, 0, 'hjy1', 0, NULL);
INSERT INTO `sys_user` VALUES ('114232163964030976', NULL, '2019-03-02 19:30:12', NULL, '2019-03-02 19:30:12', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '18751301789', NULL, '$2a$10$t8bwYZ3bCJQ9RVgeE4abT.nsMqYsg3ND6GIQTc5XtbJWJ1pt7eKwm', NULL, 0, 0, 'me', 0, NULL);
INSERT INTO `sys_user` VALUES ('114328109242650624', NULL, '2019-03-03 01:51:27', NULL, '2019-03-03 01:51:27', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '19951612819', NULL, '$2a$10$3fj9aCxDmeea5zWXd1nox.TvsIX1j.fTs7yo61rbb3k2/bUxa9nD.', NULL, 0, 0, '11111', 0, NULL);
INSERT INTO `sys_user` VALUES ('115264786224123904', NULL, '2019-03-05 15:53:28', NULL, '2019-03-06 17:56:49', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '19951612816', NULL, '$2a$10$1BbcFWOj2flqbIUg.qZEa.kYr8yUNCYVEl/Ef1xHd/T3knlLukwV.', NULL, 0, 0, 'kksama', 0, NULL);
INSERT INTO `sys_user` VALUES ('115543510215561216', NULL, '2019-03-06 10:21:01', NULL, '2019-03-06 10:21:01', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '15365568553', NULL, '$2a$10$EVBQpZvDc7CC/h4b29Jso.JydjMpCaL10vX9J6mPhX/sKF6h4rVc6', NULL, 0, 0, 'chenchen', 0, NULL);
INSERT INTO `sys_user` VALUES ('131497972226592768', '', '2019-04-19 10:58:21', '', '2019-04-28 13:48:32', '[\"110000\",\"110100\",\"110101\"]', 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, 'xswwjf@163.com', '13339080767', NULL, '$2a$10$7nLFIdMCp1XJlw09nvy1kupS1kLP5oxPes9SZgZwcavN0ITJfecnC', 0, 0, 0, 'IOT', 0, '');
INSERT INTO `sys_user` VALUES ('133406913873317888', NULL, '2019-04-24 17:23:48', NULL, '2019-04-28 13:58:44', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '13338070535', NULL, '$2a$10$rYA9l4pAmyeaVLcmBgKlBesb6fq8cw1R8aql5DEOMOnzu6260rsiC', NULL, 0, 0, 'june1', 0, NULL);
INSERT INTO `sys_user` VALUES ('134738203553828864', NULL, '2019-04-28 09:33:53', NULL, '2019-04-28 09:33:53', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '18862931497', NULL, '$2a$10$EysqJ3GmYMjcvcCvifoSfe7qtv1sZl.omudBYl..8UVQWrzGX4L12', NULL, 0, 0, 'KKKKK', 0, NULL);
INSERT INTO `sys_user` VALUES ('134752198822203392', NULL, '2019-04-28 10:29:29', NULL, '2019-04-28 10:29:29', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '15072491375', NULL, '$2a$10$hEt6QEZz7A0FweWjF2ZujurXQMzA1wSgUpZaL.LnYsEGpEku4P7oS', NULL, 0, 0, 'QQQQQQ', 0, NULL);
INSERT INTO `sys_user` VALUES ('135166945388400640', NULL, '2019-04-29 13:57:33', NULL, '2019-04-29 14:02:50', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, 'xswwjf@163.com', '13454321345', NULL, '$2a$10$afqyJJ4PLxVgBLRZWAfDreMTKG.ftKdqPWA4HKoIio9MAvbFIbwAS', 1, 0, 0, 'CS', 0, NULL);
INSERT INTO `sys_user` VALUES ('135243552824233984', NULL, '2019-04-29 19:01:57', NULL, '2019-04-29 19:02:05', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, 'xswwjf@163.com', '13456543121', NULL, '$2a$10$QFzgt0JSuBIlwUOTrblW.OACS8jGIwPfA17Pq3x4xTc7Z.2EufE0C', 1, 0, 0, 'ss', 0, NULL);
INSERT INTO `sys_user` VALUES ('135248405831094272', NULL, '2019-04-29 19:21:14', NULL, '2019-04-29 19:21:21', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, 'xswwjf@163.com', '13452145321', NULL, '$2a$10$iFl3k/.V4TL/Xrc/VONr7uPCFXqqIcQ0Msfed7e5IitzHfOeWyu0e', 1, 0, 0, 'ww', 0, NULL);
INSERT INTO `sys_user` VALUES ('135250089298890752', NULL, '2019-04-29 19:27:56', NULL, '2019-04-29 19:28:02', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, 'xswwjf@163.com', '13456543123', NULL, '$2a$10$VU6euzQyW0CC2hZKyS3eJOWDS7rwSHRVHsUipNqizkRjrFe7aZE02', 1, 0, 0, 'AA', 0, NULL);
INSERT INTO `sys_user` VALUES ('135254525576482816', NULL, '2019-04-29 19:45:33', NULL, '2019-04-29 19:45:39', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, 'xswwjf@163.com', '13421231123', NULL, '$2a$10$7qaUtdURdseNaV3Ivkc3yeCo1PeMnsSXe0ls1NBNFpsN1yq7NXjlW', 1, 0, 0, 'BB', 0, NULL);
INSERT INTO `sys_user` VALUES ('170971034135040000', NULL, '2019-08-06 09:10:12', NULL, '2019-08-06 09:10:12', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '15190860105', NULL, '$2a$10$X3ZaiMQbn9yB7Gez14MPg.sHCFryd/VBIh7oLOmlSXQ8VMd0pw3Qq', NULL, 0, 0, 'golden888', 0, NULL);
INSERT INTO `sys_user` VALUES ('682265633886208', '', '2018-11-16 21:12:40', '', '2019-06-03 15:51:12', '[\"510000\",\"510100\",\"510104\"]', 'industrial_iot/useravatar/admin/2019/06/03/1559547498712.png', NULL, 'zhangzhenya@jsxywg.cn', '19951612816', NULL, '$2a$10$4TTx.3BdlX0e12i5sRf.UOcotvPsNPfIxaEHPWR91aw5s4l.a57dO', 1, 0, 1, 'admin', 0, '1');
INSERT INTO `sys_user` VALUES ('98567535762345984', NULL, '2019-01-18 14:04:33', NULL, '2019-01-18 14:04:33', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '18761784999', NULL, '$2a$10$NmTGdaWU7Ii4WjZ2Cb3f4Oc3EMbONG6/heE5HRsug3HHd2PiyLboi', NULL, 0, 0, 'cmy003', 0, NULL);
INSERT INTO `sys_user` VALUES ('98578623442718720', NULL, '2019-01-18 14:48:37', NULL, '2019-01-18 14:48:37', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '19951612811', NULL, '$2a$10$VxfzXCY5taejL0Bej9u3Zu9vscT3Ov77GGOs6NnhgfjOTLf61zOy6', NULL, 0, 0, 'kaze', 0, NULL);
INSERT INTO `sys_user` VALUES ('98578933292732416', NULL, '2019-01-18 14:49:51', NULL, '2019-01-18 14:49:51', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '19951612812', NULL, '$2a$10$TE4FG45ja03nKXungGRi7uJbcYqL0B9bQe1Bi5AJg5bv5d1JQ.xIG', NULL, 0, 0, 'kaze666', 0, NULL);
INSERT INTO `sys_user` VALUES ('98609788241317888', NULL, '2019-01-18 16:52:27', NULL, '2019-02-18 09:49:28', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '19951612817', NULL, '$2a$10$pbo4Rk3d7GMGPSiWPQQIDea/ttOSXLfU0.Ip0BOqCQ4dsERgF9bra', NULL, 0, 0, 'zzy111', 0, NULL);
INSERT INTO `sys_user` VALUES ('98612680423641088', NULL, '2019-01-18 17:03:57', NULL, '2019-01-18 17:03:57', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '19999999999', NULL, '$2a$10$6pa0TO2ru8POg2C856odDOTRGeknTKx6tm5FQ.ETnY90lLRfR6/zy', NULL, 0, 0, 'kaza', 0, NULL);
INSERT INTO `sys_user` VALUES ('98612822711209984', NULL, '2019-01-18 17:04:30', NULL, '2019-01-18 17:04:30', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '19999999999', NULL, '$2a$10$L4dXU7shII0KBTd3PdQVeOC4bv82lCdBW7m2bhVFrryJ03mtYHD8O', NULL, 0, 0, 'kazz', 0, NULL);
INSERT INTO `sys_user` VALUES ('98613434693718016', NULL, '2019-01-18 17:06:56', NULL, '2019-01-18 17:06:56', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '19955555555', NULL, '$2a$10$eMz81herH4k18w.hufDPru87JRkiL69mbafVj7neJ98vyT0oWiu72', NULL, 0, 0, 'kkkk', 0, NULL);
INSERT INTO `sys_user` VALUES ('98614175449747456', NULL, '2019-01-18 17:09:53', NULL, '2019-01-18 17:09:53', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '19999999999', NULL, '$2a$10$4TTx.3BdlX0e12i5sRf.UOcotvPsNPfIxaEHPWR91aw5s4l.a57dO', NULL, 0, 0, 'aaaa', 0, NULL);
INSERT INTO `sys_user` VALUES ('98616690090184704', NULL, '2019-01-18 17:19:53', NULL, '2019-01-18 17:19:53', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '19999999999', NULL, '$2a$10$4TTx.3BdlX0e12i5sRf.UOcotvPsNPfIxaEHPWR91aw5s4l.a57dO', NULL, 0, 0, 'ssss', 0, NULL);
INSERT INTO `sys_user` VALUES ('98950958725206016', NULL, '2019-02-01 16:51:53', NULL, '2019-01-28 13:05:59', NULL, 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', NULL, NULL, '19999999999', NULL, '$2a$10$VsnZOi6p0z/wDtjMx0onA.YSfiwCJHrFYDOpN59kLLM7Js8C7DX9a', NULL, -1, 0, 'sss', 0, NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID雪花算法',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除标志：0：未删除，1：删除',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `role_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色ID',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('101119620164882432', NULL, NULL, 0, NULL, NULL, '95670906298306560', NULL);
INSERT INTO `sys_user_role` VALUES ('101119845763911680', NULL, NULL, 0, NULL, NULL, '95670906298306560', NULL);
INSERT INTO `sys_user_role` VALUES ('101121840860106752', NULL, NULL, 0, NULL, NULL, '95670906298306560', NULL);
INSERT INTO `sys_user_role` VALUES ('101122237381218304', NULL, NULL, 0, NULL, NULL, '95670906298306560', NULL);
INSERT INTO `sys_user_role` VALUES ('101143414824767488', NULL, NULL, 0, NULL, NULL, '95670623577051136', '96046691936899072');
INSERT INTO `sys_user_role` VALUES ('102238911912218624', NULL, NULL, 0, NULL, NULL, '95670623577051136', '102238909743763456');
INSERT INTO `sys_user_role` VALUES ('102242971188989952', NULL, NULL, 0, NULL, NULL, '95670623577051136', '102242968890511360');
INSERT INTO `sys_user_role` VALUES ('102555233627934720', NULL, NULL, 0, NULL, NULL, '496138616573952', '682265633886208');
INSERT INTO `sys_user_role` VALUES ('102615000425697280', NULL, NULL, 0, NULL, NULL, '95670623577051136', '102614998114635776');
INSERT INTO `sys_user_role` VALUES ('102850488046718976', NULL, NULL, 0, NULL, NULL, '95670906298306560', '102586466906935296');
INSERT INTO `sys_user_role` VALUES ('107558812168228864', NULL, '2019-02-12 09:32:40', 0, NULL, '2019-02-12 09:32:40', '95670623577051136', '103568068130639872');
INSERT INTO `sys_user_role` VALUES ('107558855247925248', NULL, '2019-02-12 09:32:50', 0, NULL, '2019-02-12 09:32:50', '95670906298306560', '103574420412436480');
INSERT INTO `sys_user_role` VALUES ('108666528144035840', NULL, NULL, 0, NULL, NULL, '95670906298306560', '102563712807735296');
INSERT INTO `sys_user_role` VALUES ('108711924656508928', NULL, NULL, 0, NULL, NULL, '95670906298306560', '108673485886197760');
INSERT INTO `sys_user_role` VALUES ('108761688399941632', NULL, NULL, 0, NULL, NULL, '95670906298306560', '108753195064365056');
INSERT INTO `sys_user_role` VALUES ('110907370518876160', NULL, NULL, 0, NULL, NULL, '95670906298306560', '110904503812755456');
INSERT INTO `sys_user_role` VALUES ('110912015144325120', NULL, NULL, 0, NULL, NULL, '95670906298306560', '102602936734978048');
INSERT INTO `sys_user_role` VALUES ('110925324262313984', NULL, NULL, 0, NULL, NULL, '95670906298306560', '102964571676872704');
INSERT INTO `sys_user_role` VALUES ('111214062305546240', NULL, NULL, 0, NULL, NULL, '95670906298306560', '102609512803864576');
INSERT INTO `sys_user_role` VALUES ('112259358959603712', NULL, NULL, 0, NULL, NULL, '95670906298306560', '103208037287727104');
INSERT INTO `sys_user_role` VALUES ('112642882359267328', NULL, NULL, 0, NULL, NULL, '95670906298306560', '98616690090184704');
INSERT INTO `sys_user_role` VALUES ('113446855144116224', NULL, NULL, 0, NULL, NULL, '95670906298306560', '113440042856222720');
INSERT INTO `sys_user_role` VALUES ('114224744324141056', NULL, NULL, 0, NULL, NULL, '95670623577051136', '114224742218600448');
INSERT INTO `sys_user_role` VALUES ('114232166031822848', NULL, NULL, 0, NULL, NULL, '95670623577051136', '114232163964030976');
INSERT INTO `sys_user_role` VALUES ('114328111356579840', NULL, NULL, 0, NULL, NULL, '95670623577051136', '114328109242650624');
INSERT INTO `sys_user_role` VALUES ('115264788296110080', NULL, NULL, 0, NULL, NULL, '95670623577051136', '115264786224123904');
INSERT INTO `sys_user_role` VALUES ('115545563805519872', NULL, NULL, 0, NULL, NULL, '95670906298306560', '115543510215561216');
INSERT INTO `sys_user_role` VALUES ('131549321026867200', NULL, NULL, 0, NULL, NULL, '95670906298306560', '131497972226592768');
INSERT INTO `sys_user_role` VALUES ('133406916050161664', NULL, NULL, 0, NULL, NULL, '95670623577051136', '133406913873317888');
INSERT INTO `sys_user_role` VALUES ('134738206305292288', NULL, NULL, 0, NULL, NULL, '95670623577051136', '134738203553828864');
INSERT INTO `sys_user_role` VALUES ('134819616642830336', NULL, NULL, 0, NULL, NULL, '95670906298306560', '134752198822203392');
INSERT INTO `sys_user_role` VALUES ('135232003611037696', NULL, NULL, 0, NULL, NULL, '95670906298306560', '135166945388400640');
INSERT INTO `sys_user_role` VALUES ('135245573912530944', NULL, NULL, 0, NULL, NULL, '95670906298306560', '135243552824233984');
INSERT INTO `sys_user_role` VALUES ('135248407764668416', NULL, '2019-04-29 19:21:14', 0, NULL, '2019-04-29 19:21:14', '95670906298306560', '135248405831094272');
INSERT INTO `sys_user_role` VALUES ('135250091123412992', NULL, '2019-04-29 19:27:56', 0, NULL, '2019-04-29 19:27:56', '95670906298306560', '135250089298890752');
INSERT INTO `sys_user_role` VALUES ('135254527480696832', NULL, '2019-04-29 19:45:33', 0, NULL, '2019-04-29 19:45:33', '95670906298306560', '135254525576482816');
INSERT INTO `sys_user_role` VALUES ('16056421829316608', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', '496138616573953', '6118792149078016');
INSERT INTO `sys_user_role` VALUES ('170971036873920512', NULL, NULL, 0, NULL, NULL, '95670623577051136', '170971034135040000');
INSERT INTO `sys_user_role` VALUES ('64917361706143744', NULL, NULL, 0, NULL, NULL, '496138616573953', '64917347336458240');
INSERT INTO `sys_user_role` VALUES ('66730604309254144', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', '496138616573953', '66676920745463808');
INSERT INTO `sys_user_role` VALUES ('70322000291172352', NULL, NULL, 0, NULL, NULL, '2018-11-15 11:20:58', '70321985741131776');
INSERT INTO `sys_user_role` VALUES ('70331415572516864', NULL, NULL, 0, NULL, NULL, '2018-11-15 11:20:58', '70331413257261056');
INSERT INTO `sys_user_role` VALUES ('73134170557124608', NULL, NULL, 0, NULL, NULL, '2018-11-15 11:20:58', '73134169726652416');
INSERT INTO `sys_user_role` VALUES ('73134611768545280', NULL, NULL, 0, NULL, NULL, '2018-11-15 11:20:58', '73134610015326208');
INSERT INTO `sys_user_role` VALUES ('73135073234259968', NULL, NULL, 0, NULL, NULL, '2018-11-15 11:20:58', '73135072319901696');
INSERT INTO `sys_user_role` VALUES ('73161336074276864', NULL, NULL, 0, NULL, NULL, '2018-11-15 11:20:58', '73161335013117952');
INSERT INTO `sys_user_role` VALUES ('74419147710140416', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', '496138616573953', '74419146460237824');
INSERT INTO `sys_user_role` VALUES ('74953298301423616', NULL, '2018-11-15 11:20:58', 0, NULL, '2018-11-15 11:20:58', '496138616573953', '74656061688123392');
INSERT INTO `sys_user_role` VALUES ('75008177640312832', NULL, NULL, 0, NULL, NULL, '496138616573953', '75008176751120384');
INSERT INTO `sys_user_role` VALUES ('98567538182459392', NULL, NULL, 0, NULL, NULL, '95670623577051136', '98567535762345984');
INSERT INTO `sys_user_role` VALUES ('98578626923991040', NULL, NULL, 0, NULL, NULL, '95670623577051136', '98578623442718720');
INSERT INTO `sys_user_role` VALUES ('98578935545073664', NULL, NULL, 0, NULL, NULL, '95670623577051136', '98578933292732416');
INSERT INTO `sys_user_role` VALUES ('98609790296526848', NULL, NULL, 0, NULL, NULL, '95670623577051136', '98609788241317888');
INSERT INTO `sys_user_role` VALUES ('98612682411741184', NULL, NULL, 0, NULL, NULL, '95670623577051136', '98612680423641088');
INSERT INTO `sys_user_role` VALUES ('98612824728670208', NULL, NULL, 0, NULL, NULL, '95670623577051136', '98612822711209984');
INSERT INTO `sys_user_role` VALUES ('98613436895727616', NULL, NULL, 0, NULL, NULL, '95670623577051136', '98613434693718016');
INSERT INTO `sys_user_role` VALUES ('98614177496567808', NULL, NULL, 0, NULL, NULL, '95670623577051136', '98614175449747456');
INSERT INTO `sys_user_role` VALUES ('98950960830746624', NULL, NULL, 0, NULL, NULL, '95670623577051136', '98950958725206016');

-- ----------------------------
-- Table structure for t_account_info_company
-- ----------------------------
DROP TABLE IF EXISTS `t_account_info_company`;
CREATE TABLE `t_account_info_company`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `individual_id` int(11) NULL DEFAULT NULL COMMENT '申请人id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `short_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业简称',
  `website` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站',
  `credit_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统一设备信用代码',
  `credit_photo` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业营业执照',
  `company_description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业描述',
  `annual_turnover` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年营业额',
  `team` int(11) NULL DEFAULT NULL COMMENT '软件研发团队',
  `star_product` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '以往明星产品',
  `product_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `product_type` int(11) NULL DEFAULT NULL COMMENT '产品类型',
  `link_type` int(11) NULL DEFAULT NULL COMMENT '联网方式',
  `operate_system` int(11) NULL DEFAULT NULL COMMENT '操作系统',
  `product_description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品描述',
  `reject_reason` varchar(1600) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '驳回理由',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '企业用户基本信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_account_info_company_description
-- ----------------------------
DROP TABLE IF EXISTS `t_account_info_company_description`;
CREATE TABLE `t_account_info_company_description`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `company_id` int(11) NULL DEFAULT NULL COMMENT '公司id',
  `attachment` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '企业描述附件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_account_info_company_product_description
-- ----------------------------
DROP TABLE IF EXISTS `t_account_info_company_product_description`;
CREATE TABLE `t_account_info_company_product_description`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `company_id` int(11) NULL DEFAULT NULL COMMENT '公司id',
  `attachment` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品描述附件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_account_info_company_star_product
-- ----------------------------
DROP TABLE IF EXISTS `t_account_info_company_star_product`;
CREATE TABLE `t_account_info_company_star_product`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `company_id` int(11) NULL DEFAULT NULL COMMENT '公司id',
  `attachment` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '明星产品附件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_account_info_individual
-- ----------------------------
DROP TABLE IF EXISTS `t_account_info_individual`;
CREATE TABLE `t_account_info_individual`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `type` int(11) NULL DEFAULT NULL COMMENT '会员身份',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `product_type` int(11) NULL DEFAULT NULL COMMENT '行业应用',
  `major_business` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主营业务',
  `identity_no` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `identity_photo` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证图片地址',
  `staff` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务',
  `province` int(11) NULL DEFAULT NULL COMMENT '省份',
  `city` int(11) NULL DEFAULT NULL COMMENT '城市',
  `district` int(11) NULL DEFAULT NULL COMMENT '区',
  `street` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '街道',
  `tel` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `fax` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '传真',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `source` int(11) NULL DEFAULT NULL COMMENT '来源',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  `audit_status` int(1) NULL DEFAULT NULL COMMENT '审核状态: 0:未申请,1审核中,2审核通过,3,审核未通过,',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属用户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '个人用户基本信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_app_info
-- ----------------------------
DROP TABLE IF EXISTS `t_app_info`;
CREATE TABLE `t_app_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `app_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用标识符',
  `access_secret` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权密钥',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者id',
  `create_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者名称',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者id',
  `modify_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_config_file
-- ----------------------------
DROP TABLE IF EXISTS `t_device_config_file`;
CREATE TABLE `t_device_config_file`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '升级包名称',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '产品名称',
  `version` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '版本',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '升级包地址',
  `file_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_config_file_record
-- ----------------------------
DROP TABLE IF EXISTS `t_device_config_file_record`;
CREATE TABLE `t_device_config_file_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `device_id` int(11) NULL DEFAULT NULL COMMENT '设备名称',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '产品名称',
  `package_id` int(11) NULL DEFAULT NULL COMMENT '升级包id(配置文件id)',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置文件存储地址',
  `issued_flag` int(11) NULL DEFAULT NULL COMMENT '下发标识:0未下发,1已下发,2操作结果成功3.操作结果失败',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_error_log
-- ----------------------------
DROP TABLE IF EXISTS `t_device_error_log`;
CREATE TABLE `t_device_error_log`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `device_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `product_key` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `company` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `product_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `device_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `product_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `details` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_del` int(1) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_user_name` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modify_user_name` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_event_param_record
-- ----------------------------
DROP TABLE IF EXISTS `t_device_event_param_record`;
CREATE TABLE `t_device_event_param_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `param_id` int(11) NULL DEFAULT 0 COMMENT '属性id',
  `record_id` int(11) NULL DEFAULT 0 COMMENT '明细id',
  `data_value` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '数据',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_event_record
-- ----------------------------
DROP TABLE IF EXISTS `t_device_event_record`;
CREATE TABLE `t_device_event_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NULL DEFAULT NULL COMMENT '方法id',
  `trigger_time` datetime(0) NULL DEFAULT NULL COMMENT '触发时间',
  `device_id` int(11) NULL DEFAULT 0 COMMENT '设备id',
  `in_params` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_info
-- ----------------------------
DROP TABLE IF EXISTS `t_device_info`;
CREATE TABLE `t_device_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字',
  `device_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '产品id',
  `device_secret` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备密钥',
  `status` int(11) NULL DEFAULT NULL COMMENT '设备状态 1：未激活 2：离线 3：在线',
  `ip` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `version` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '固件版本',
  `add_time` timestamp(0) NULL DEFAULT NULL COMMENT '添加日期',
  `activation_time` timestamp(0) NULL DEFAULT NULL COMMENT '激活日期',
  `address` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址 省|市',
  `position` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '位置',
  `last_time` datetime(0) NULL DEFAULT NULL,
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_method_param_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_device_method_param_detail`;
CREATE TABLE `t_device_method_param_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `param_id` int(11) NULL DEFAULT 0 COMMENT '属性id',
  `record_id` int(11) NULL DEFAULT 0 COMMENT '明细id',
  `data_value` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '数据',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_method_record
-- ----------------------------
DROP TABLE IF EXISTS `t_device_method_record`;
CREATE TABLE `t_device_method_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `method_id` int(11) NULL DEFAULT NULL COMMENT '方法id',
  `call_time` datetime(0) NULL DEFAULT NULL COMMENT '调用时间',
  `device_id` int(11) NULL DEFAULT NULL COMMENT '设备id',
  `status` int(1) NULL DEFAULT 0 COMMENT '方法执行状态 0 未返回 1已返回 2超时',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '反馈时间',
  `in_params` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入参',
  `out_params` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出参',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_property_record
-- ----------------------------
DROP TABLE IF EXISTS `t_device_property_record`;
CREATE TABLE `t_device_property_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `collect_time` datetime(0) NULL DEFAULT NULL COMMENT '采集时间',
  `device_id` int(11) NULL DEFAULT 0 COMMENT '设备id',
  `data_detail` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '值',
  `alarm_detail` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报警',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_property_record_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_device_property_record_detail`;
CREATE TABLE `t_device_property_record_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `property_id` int(11) NULL DEFAULT 0 COMMENT '属性id',
  `data_value` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '数据',
  `is_alarm` int(1) NULL DEFAULT NULL COMMENT '是否报警',
  `record_id` int(11) NULL DEFAULT 0 COMMENT '设备id',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_remote_config
-- ----------------------------
DROP TABLE IF EXISTS `t_device_remote_config`;
CREATE TABLE `t_device_remote_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NOT NULL COMMENT '所属产品id',
  `json_string` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置的json字符串',
  `is_del` int(1) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_remote_config_record
-- ----------------------------
DROP TABLE IF EXISTS `t_device_remote_config_record`;
CREATE TABLE `t_device_remote_config_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `config_id` int(11) NULL DEFAULT NULL COMMENT 'json 配置id',
  `product_id` int(11) NOT NULL COMMENT '所属产品id',
  `device_id` int(11) NOT NULL COMMENT '设备id',
  `json_string` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置的json字符串',
  `issued_flag` int(1) NULL DEFAULT NULL COMMENT '下发标识:0未下发,1已下发,2操作结果成功3.操作结果失败',
  `is_del` int(1) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_upgrade_package
-- ----------------------------
DROP TABLE IF EXISTS `t_device_upgrade_package`;
CREATE TABLE `t_device_upgrade_package`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '升级包名称',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '产品名称',
  `version` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '版本',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '升级包地址',
  `file_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_upgrade_record
-- ----------------------------
DROP TABLE IF EXISTS `t_device_upgrade_record`;
CREATE TABLE `t_device_upgrade_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `device_id` int(11) NULL DEFAULT NULL COMMENT '设备名称',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '产品名称',
  `package_id` int(11) NULL DEFAULT NULL COMMENT '升级包id',
  `issued_flag` int(11) NULL DEFAULT NULL COMMENT '下发标识:0未下发,1已下发,2操作结果成功3.操作结果失败',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_master_event
-- ----------------------------
DROP TABLE IF EXISTS `t_master_event`;
CREATE TABLE `t_master_event`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '产品类型',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字',
  `key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标识符',
  `event_type` int(11) NULL DEFAULT NULL COMMENT '事件类别 1:信息 2：预警 3：故障',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_master_event_param
-- ----------------------------
DROP TABLE IF EXISTS `t_master_event_param`;
CREATE TABLE `t_master_event_param`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `event_id` int(11) NULL DEFAULT NULL COMMENT '事件id',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字',
  `key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标识符',
  `data_type` int(11) NULL DEFAULT NULL COMMENT '数据类型(1:int 2:float 3:double 4:bool 5:text 6:timestamps)',
  `min` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最小值',
  `max` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最大值',
  `step` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '步长 ',
  `bool_false` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'false对用的含义',
  `bool_true` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'true对用的含义',
  `data_length` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据长度',
  `unit` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_master_link_type
-- ----------------------------
DROP TABLE IF EXISTS `t_master_link_type`;
CREATE TABLE `t_master_link_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_del` int(1) NULL DEFAULT NULL,
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modify_time` timestamp(0) NULL DEFAULT NULL,
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典---联网方式' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_master_measure_unit
-- ----------------------------
DROP TABLE IF EXISTS `t_master_measure_unit`;
CREATE TABLE `t_master_measure_unit`  (
  `id` int(11) NOT NULL COMMENT '自增',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_master_method
-- ----------------------------
DROP TABLE IF EXISTS `t_master_method`;
CREATE TABLE `t_master_method`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '产品类型',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字',
  `key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标识符',
  `is_sync` int(11) NULL DEFAULT NULL COMMENT '是否同步 1:同步 2：异步',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_master_method_param
-- ----------------------------
DROP TABLE IF EXISTS `t_master_method_param`;
CREATE TABLE `t_master_method_param`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `method_id` int(11) NULL DEFAULT NULL COMMENT '方法id',
  `param_type` int(11) NULL DEFAULT NULL COMMENT '参数类型 1：输入 2:输出',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字',
  `key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标识符',
  `data_type` int(11) NULL DEFAULT NULL COMMENT '数据类型(1:int 2:float 3:double 4:bool 5:text 6:timestamps)',
  `min` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最小值',
  `max` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最大值',
  `step` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '步长 ',
  `bool_false` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'false对用的含义',
  `bool_true` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'true对用的含义',
  `data_length` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据长度',
  `unit` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_master_operate_system
-- ----------------------------
DROP TABLE IF EXISTS `t_master_operate_system`;
CREATE TABLE `t_master_operate_system`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_master_property
-- ----------------------------
DROP TABLE IF EXISTS `t_master_property`;
CREATE TABLE `t_master_property`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '产品类型',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字',
  `key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标识符',
  `data_type` int(11) NULL DEFAULT NULL COMMENT '数据类型(1:int 2:float 3:double 4:bool 5:text 6:timestamps)',
  `min` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最小值',
  `max` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最大值',
  `step` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '步长 ',
  `bool_false` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'false对用的含义',
  `bool_true` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'true对用的含义',
  `data_length` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据长度',
  `wr_type` int(11) NULL DEFAULT NULL COMMENT '读写类型 1读写 2只读',
  `unit` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_master_type
-- ----------------------------
DROP TABLE IF EXISTS `t_master_type`;
CREATE TABLE `t_master_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `parent_id` int(11) NULL DEFAULT NULL,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_product_event
-- ----------------------------
DROP TABLE IF EXISTS `t_product_event`;
CREATE TABLE `t_product_event`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '事件名',
  `key` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '标识符 （json对应key）\r\n  ',
  `event_type` int(1) NULL DEFAULT 0 COMMENT '事件类型\r\n  1信息\r\n  2告警\r\n  3故障',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '描述',
  `product_id` int(11) NULL DEFAULT 0 COMMENT '产品id',
  `is_del` int(1) NULL DEFAULT 0 COMMENT '删除',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_product_event_param
-- ----------------------------
DROP TABLE IF EXISTS `t_product_event_param`;
CREATE TABLE `t_product_event_param`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '属性名称',
  `key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '标识符(json对应的key)',
  `data_type` int(2) NULL DEFAULT 0 COMMENT '数据类型\r\n  1:int32\r\n  2:float\r\n  3:double\r\n  4:bool\r\n  5:text\r\n  6:date\r\n',
  `min` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最小值\r\n  为int32时 取值为整形\r\n  为float/double 取值可为浮点型',
  `max` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最大值',
  `step` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '步长\r\n  当为int32时 步长为整形\r\n  当为float/double时 步长为浮点型',
  `bool_false` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'bool0代表的含义',
  `bool_true` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'bool1代表的含义',
  `data_length` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '数据长度\r\n  当选择text类型时必填',
  `event_id` int(11) NULL DEFAULT 0 COMMENT '产品方法id',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '描述',
  `unit` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '单位',
  `is_del` int(1) NULL DEFAULT 0 COMMENT '删除',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_product_info
-- ----------------------------
DROP TABLE IF EXISTS `t_product_info`;
CREATE TABLE `t_product_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '产品名',
  `type` int(11) NULL DEFAULT 0 COMMENT '分类',
  `product_key` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '产品唯一编号(由服务端生成)',
  `product_secret` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '产品密钥(随机生成)',
  `dynamic_registration` int(1) NULL DEFAULT 0 COMMENT '动态注册',
  `status` int(1) NULL DEFAULT 0 COMMENT '产品状态(0 开发中   1已发布  )',
  `version` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '修改人',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '产品描述',
  `node_type` int(1) NULL DEFAULT 0 COMMENT '节点类型\r\n0 设备\r\n1 网关',
  `link_type` int(11) NULL DEFAULT NULL,
  `is_del` int(1) NULL DEFAULT 0 COMMENT '删除',
  `reader_idle_time` bigint(11) NULL DEFAULT 0 COMMENT '闲置时间(单位秒)',
  `create_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建人',
  `modify_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `product_u_key`(`product_key`) USING BTREE COMMENT 'product_key 唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_product_method
-- ----------------------------
DROP TABLE IF EXISTS `t_product_method`;
CREATE TABLE `t_product_method`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `key` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '标识符（json对应的key）',
  `is_sync` int(1) NULL DEFAULT 0 COMMENT '同步\r\n  0：异步\r\n  1：同步',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '描述',
  `product_id` int(11) NULL DEFAULT 0 COMMENT '产品id',
  `is_del` int(1) NULL DEFAULT 0 COMMENT '删除',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_product_method_param
-- ----------------------------
DROP TABLE IF EXISTS `t_product_method_param`;
CREATE TABLE `t_product_method_param`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '属性名称',
  `key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '标识符(json对应的key)',
  `data_type` int(2) NULL DEFAULT 0 COMMENT '数据类型\r\n  1:int32\r\n  2:float\r\n  3:double\r\n  4:bool\r\n  5:text\r\n  6:date\r\n',
  `min` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最小值\r\n  为int32时 取值为整形\r\n  为float/double 取值可为浮点型',
  `max` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最大值',
  `step` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '步长\r\n  当为int32时 步长为整形\r\n  当为float/double时 步长为浮点型',
  `bool_false` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'false代表的含义',
  `bool_true` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'true代表的含义',
  `data_length` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '数据长度\r\n  当选择text类型时必填',
  `param_type` int(1) NULL DEFAULT 0 COMMENT '参数类型\r\n  1:入参\r\n  2:出参',
  `method_id` int(11) NULL DEFAULT 0 COMMENT '产品方法id',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '描述',
  `unit` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '单位',
  `is_del` int(1) NULL DEFAULT 0 COMMENT '删除',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_product_property
-- ----------------------------
DROP TABLE IF EXISTS `t_product_property`;
CREATE TABLE `t_product_property`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '属性名称',
  `inner_key` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内部编号',
  `key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '标识符(json对应的key)',
  `data_type` int(2) NULL DEFAULT 0 COMMENT '数据类型\r\n  1:int32\r\n  2:float\r\n  3:double\r\n  4:bool\r\n  5:text\r\n  6:date\r\n',
  `min` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最小值\r\n  为int32时 取值为整形\r\n  为float/double 取值可为浮点型',
  `max` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最大值',
  `step` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '步长\r\n  当为int32时 步长为整形\r\n  当为float/double时 步长为浮点型',
  `bool_false` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'bool0代表的含义',
  `bool_true` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'bool1代表的含义',
  `data_length` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '数据长度\r\n  当选择text类型时必填',
  `wr_type` int(1) NULL DEFAULT 0 COMMENT '读写类型',
  `product_id` int(11) NULL DEFAULT 0 COMMENT '产品id',
  `unit` int(11) NULL DEFAULT 0 COMMENT '单位',
  `is_del` int(1) NULL DEFAULT 0 COMMENT '删除',
  `alarm_expression` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '报警表达式',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_report_day
-- ----------------------------
DROP TABLE IF EXISTS `t_report_day`;
CREATE TABLE `t_report_day`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_account` int(11) NULL DEFAULT NULL COMMENT '数据量',
  `total` int(11) NULL DEFAULT NULL COMMENT '累计数量',
  `add_account` int(11) NULL DEFAULT NULL COMMENT '新增数量',
  `report_date` date NULL DEFAULT NULL COMMENT '统计日期',
  `report_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统计人',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标识 默认0:未删除 1:已删除',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `modify_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `modify_user_name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
