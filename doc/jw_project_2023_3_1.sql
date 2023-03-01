/*
 Navicat Premium Data Transfer

 Source Server         : 刘俊志的数据库
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : 101.201.196.173:3307
 Source Schema         : jw_project

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 01/03/2023 16:34:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog_article
-- ----------------------------
DROP TABLE IF EXISTS `blog_article`;
CREATE TABLE `blog_article`  (
  `article_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `user_id` int(11) NOT NULL COMMENT '作者ID',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '类别ID',
  `is_top` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '顶置（0表示不顶置，1表示顶置）',
  `article_cover` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '文章封面',
  `article_title` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '文章标题',
  `article_summary` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '文章简介',
  `article_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文章内容',
  `article_visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '可见（0代表仅自己可见，1表示所有人可见）',
  `view_counts` int(11) NULL DEFAULT 0 COMMENT '浏览数量',
  `comment_allowed` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否允许评论(0表示不能评论，1表示可以评论)',
  `article_check` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '文章审核状态（1表示通过，0表示未审核，f表示未通过）',
  `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否删除(0代表存在 1代表删除)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`article_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '博客文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_article
-- ----------------------------
INSERT INTO `blog_article` VALUES (32, 8, 5, '0', '/static/upload/blog/article/cover/2023-01-26-b70a470ce19c4587b02daf207b037545.jpg', '图片测试', '123', '222\n\n\n已上传一张图片\n![蜡笔小新3取消测试.jpeg](/static/upload/blog/article/context/image/2023-01-26-2833efdb72634aabb3ade268a63de611.jpeg)\n\n\n\n', '1', 8, '0', '1', '0', '2023-01-26 13:12:56', 'jingwen', '2023-03-01 09:56:25');
INSERT INTO `blog_article` VALUES (38, 8, 11, '1', '/static/upload/blog/article/cover/2023-01-26-f6ea84a4576e4f0fbfd470f977ac6994.png', '审核不通过文章——标题长度溢出测试——审核不通过文章——标题长度溢出测试', '审核不通过文章', '**审核不通过文章**\n*斜体*\n# 一级标题\n\n## ++二~~级~~标题++\n\n==标记==\n\nX^2^   Y~2~\n::: hljs-left\n\n左对齐\n\n:::\n::: hljs-center\n\n  居中\n\n:::\n::: hljs-right\n\n  右对齐\n\n:::\n### 三级标题\n> 段落引用\n1. youxu\n2. 2\n- 123\n- 无序\n```java\n//代码块\n/**\n* 博客后台新增文章\n**/\n@Override\npublic void addBlogArticle(BlogAdminAddArticleDTO blogAdminAddArticleDTO) {\n    BlogArticle addArticle = new BlogArticle();\n    BeanUtil.copyProperties(blogAdminAddArticleDTO, addArticle);\n    save(addArticle);\n}\n```\n------------------------------------\n表情😀😁😂🤣\n------------------------------------\n![测试专用封面2.png](/static/upload/blog/article/context/image/2023-01-27-c90e4643131640bab212f38951ff9dd7.png)\n\n\n行内：$x+y^{2x}$\n\n$$\\Gamma(z) = \\int_0^\\infty t^{z-1}e^{-t}dt\\,.$$', '1', 18, '1', '1', '0', '2023-01-26 17:29:22', 'jingwen', '2023-02-20 17:06:28');

-- ----------------------------
-- Table structure for blog_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `blog_article_tag`;
CREATE TABLE `blog_article_tag`  (
  `article_id` int(11) NOT NULL COMMENT '文章ID',
  `tag_id` int(11) NOT NULL COMMENT '标签ID\r\n',
  PRIMARY KEY (`article_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '博客标签关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_article_tag
-- ----------------------------
INSERT INTO `blog_article_tag` VALUES (32, 1);
INSERT INTO `blog_article_tag` VALUES (38, 1);
INSERT INTO `blog_article_tag` VALUES (38, 2);

-- ----------------------------
-- Table structure for blog_category
-- ----------------------------
DROP TABLE IF EXISTS `blog_category`;
CREATE TABLE `blog_category`  (
  `category_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类别ID',
  `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '类别名称',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  `category_sort` int(11) NULL DEFAULT NULL COMMENT '导航栏分类优先级：数字小的在前面',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '状态(1正常 0停用)',
  `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否删除(0代表存在 1代表删除)',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章类别表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_category
-- ----------------------------
INSERT INTO `blog_category` VALUES (5, '类别测试', '', 4, '1', '0', 'jingwen', '2023-01-13 13:18:54', 'jingwen', '2023-03-01 15:38:23');
INSERT INTO `blog_category` VALUES (11, '图片', '', 1, '1', '0', 'jingwen', '2023-01-26 12:26:46', 'jingwen', '2023-01-26 12:26:46');

-- ----------------------------
-- Table structure for blog_comment
-- ----------------------------
DROP TABLE IF EXISTS `blog_comment`;
CREATE TABLE `blog_comment`  (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `comment_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论类型',
  `user_id` int(11) NOT NULL COMMENT '评论用户ID',
  `article_id` int(11) NULL DEFAULT NULL COMMENT '文章ID',
  `floor_comment_id` int(11) NULL DEFAULT 0 COMMENT '楼层评论ID(0表示为第1级评论)',
  `reply_comment_id` int(11) NULL DEFAULT 0 COMMENT '回复的评论ID(0表示为第1级评论)',
  `reply_user_id` int(11) NULL DEFAULT 0 COMMENT '回复的用户ID(0表示为第1级评论)',
  `comment_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评论内容',
  `comment_check` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '评论审核状态（1表示通过，0表示未通过）',
  `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否删除(0代表存在 1代表删除)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 155 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '博客评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_comment
-- ----------------------------
INSERT INTO `blog_comment` VALUES (155, 'article', 24, 38, 0, 0, 0, '评论测试[default:衰][ppx:xiaochou@3x]\n<bloguser,/static/upload/blog/comments/2023-03-01-809406f730d74791868afd632bafbcda.jpeg>', '1', '0', '2023-03-01 15:59:35', NULL, '2023-03-01 15:59:35');
INSERT INTO `blog_comment` VALUES (156, 'article', 24, 38, 155, 155, 24, '回复测试[default:摸鱼]', '1', '0', '2023-03-01 15:59:53', NULL, '2023-03-01 15:59:53');
INSERT INTO `blog_comment` VALUES (157, 'message', 24, 0, 0, 0, 0, '留言评论测试[ppx:nianyu@3x]', '1', '0', '2023-03-01 16:00:41', NULL, '2023-03-01 16:00:41');

-- ----------------------------
-- Table structure for blog_friend
-- ----------------------------
DROP TABLE IF EXISTS `blog_friend`;
CREATE TABLE `blog_friend`  (
  `friend_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `friend_title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '标题',
  `friend_cover` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面',
  `friend_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链接',
  `friend_introduction` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '简介',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '状态(1正常 f停用, 0待定)',
  `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否删除(0代表存在 1代表删除)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`friend_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '友链表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_friend
-- ----------------------------

-- ----------------------------
-- Table structure for blog_message
-- ----------------------------
DROP TABLE IF EXISTS `blog_message`;
CREATE TABLE `blog_message`  (
  `message_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '留言ID',
  `message_nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '昵称',
  `message_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '留言地址',
  `message_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '留言邮箱',
  `message_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '留言内容',
  `message_check` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '留言审核状态（1表示通过，0表示未审核）',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`message_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '弹幕留言表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_message
-- ----------------------------
INSERT INTO `blog_message` VALUES (7, '博客用户', '/static/upload/avatar/2023-02-26-73cfdb278f9e44ffb3519ae92107d59d.jpg', 'bloguser@qq.com', '留言板测试', '1', '2023-03-01 16:00:23');

-- ----------------------------
-- Table structure for blog_moments
-- ----------------------------
DROP TABLE IF EXISTS `blog_moments`;
CREATE TABLE `blog_moments`  (
  `moments_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '朋友圈ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `moments_content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '内容',
  `is_public` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否公开(0代表私密 1代表公开)',
  `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否删除(0代表存在 1代表删除)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`moments_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '朋友圈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_moments
-- ----------------------------

-- ----------------------------
-- Table structure for blog_tag
-- ----------------------------
DROP TABLE IF EXISTS `blog_tag`;
CREATE TABLE `blog_tag`  (
  `tag_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '标签ID\r\n',
  `tag_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '标签名称',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '状态(1正常 0停用)',
  `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否删除(0代表存在 1代表删除)',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '博客标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_tag
-- ----------------------------
INSERT INTO `blog_tag` VALUES (1, '标签1', '标签1备注', '1', '0', '', '2023-02-28 12:51:34', '', NULL);
INSERT INTO `blog_tag` VALUES (2, '标签2', '标签2备注', '1', '0', '', '2023-02-28 12:51:37', '', NULL);

-- ----------------------------
-- Table structure for blog_web
-- ----------------------------
DROP TABLE IF EXISTS `blog_web`;
CREATE TABLE `blog_web`  (
  `web_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '网站配置ID',
  `web_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网站名称',
  `web_title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网站标题',
  `web_notices` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '网站公告',
  `web_footer` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网站页脚',
  `background_image` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '背景',
  `web_avatar` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网站头像',
  `random_avatar` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '随机头像',
  `random_name` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '随机名称',
  `random_cover` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '随机封面',
  `waifu_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '看板娘消息',
  `article_check` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '开启文章审核(1:不开启, 0:开启)',
  `comment_check` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '开启评论审核(1:不开启, 0:开启)',
  `message_check` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '开启留言审核(1:不开启, 0:开启)',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否启用[0:否，1:是]',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`web_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '网站配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_web
-- ----------------------------
INSERT INTO `blog_web` VALUES (1, '🐭妙妙屋', 'jingwen', '嘿，你丫瞅什么呢✋🤡🤚 ', '♿冲刺 ♿冲刺 ♿冲刺 ♿冲刺 ♿冲刺', '/static/upload/blog/blogWeb/2023-03-01-23f67579a900498d9880492e17f7bdd3.jpg', '/static/upload/blog/blogWeb/2023-03-01-38696aa383464b05a2e0220ce10b1f04.jpeg', '[]', '[]', '[]', '{}', '1', '1', '1', '1', 'jingwen', '2023-03-01 15:50:54');

-- ----------------------------
-- Table structure for dl_face_database
-- ----------------------------
DROP TABLE IF EXISTS `dl_face_database`;
CREATE TABLE `dl_face_database`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '父id',
  `face_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '人脸名',
  `face_sex` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '人脸性别',
  `face_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '人脸类别',
  `image_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片名称',
  `image_address` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片地址',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '状态(1正常 0停用)',
  `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否删除(0代表存在 1代表删除)',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '人脸库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dl_face_database
-- ----------------------------
INSERT INTO `dl_face_database` VALUES (24, 0, '胡歌', '', '', '胡歌', '/static/upload/deep_learning/face_detect/face_database/defined_face/胡歌_1.png', '', '1', '0', 'jingwen', '2022-09-22 15:51:17', 'jingwen', '2022-09-24 13:55:04');
INSERT INTO `dl_face_database` VALUES (30, 0, '黄日华', '', '', '黄日华', '/static/upload/deep_learning/face_detect/face_database/defined_face/黄日华_1.png', '', '1', '0', 'jingwen', '2022-09-23 09:27:40', 'jingwen', '2022-09-23 09:27:40');
INSERT INTO `dl_face_database` VALUES (31, 0, '翁美玲', '', '', '翁美玲', '/static/upload/deep_learning/face_detect/face_database/defined_face/翁美玲_1.png', '', '1', '0', 'jingwen', '2022-09-23 09:32:55', 'jingwen', '2022-09-23 09:32:55');

-- ----------------------------
-- Table structure for dl_face_detect_file
-- ----------------------------
DROP TABLE IF EXISTS `dl_face_detect_file`;
CREATE TABLE `dl_face_detect_file`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '检测文件ID',
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '检测文件名',
  `file_address` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '检测文件地址',
  `file_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '检测文件类别',
  `detect_status` int(11) NULL DEFAULT 0 COMMENT '检测状态(0:未检测, 1:检测中, 2:检测完成, -1:检测失败)',
  `save_result` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否保存检测结果(0:否, 1:是)',
  `result_file_address` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '检测结果文件地址',
  `result_msg` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '检测结果描述',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否删除(0代表存在 1代表删除)',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '检测人脸文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dl_face_detect_file
-- ----------------------------
INSERT INTO `dl_face_detect_file` VALUES (38, '郭靖黄蓉', '/static/upload/deep_learning/face_detect/face_detect_file/to_detect_file/2022-09-24-cccf747821b64b93916626fd1bde1c54_undetected.jpg', 'image', 2, '1', '/static/upload/deep_learning/face_detect/face_detect_file/detect_file_result/2022-09-24-cccf747821b64b93916626fd1bde1c54_detected.jpg', '检测到的人脸: [\"黄日华\",\"翁美玲\"]', NULL, '0', 'jingwen', '2022-09-24 13:39:55', 'jingwen', '2022-09-24 13:39:58');
INSERT INTO `dl_face_detect_file` VALUES (39, '胡歌', '/static/upload/deep_learning/face_detect/face_detect_file/to_detect_file/2022-09-24-a60d675d028f4425a9f95fd1dd67aeee_undetected.mp4', 'video', 2, '1', '/static/upload/deep_learning/face_detect/face_detect_file/detect_file_result/2022-09-24-a60d675d028f4425a9f95fd1dd67aeee_detected_voice.mp4', '检测到的人脸: [\"胡歌\"]', NULL, '0', 'jingwen', '2022-09-24 13:51:57', 'jingwen', '2022-09-24 13:52:12');

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '登录日志ID',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录账户',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录ip',
  `login_location` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作地址',
  `login_browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录浏览器',
  `login_os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` int(11) NULL DEFAULT 0 COMMENT '登录状态（0正常 1异常）',
  `login_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录结果',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 443 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户登录日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES (454, 'bloguser', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2023-03-01 15:57:01');
INSERT INTO `sys_login_log` VALUES (455, 'bloguser', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2023-03-01 15:57:08');
INSERT INTO `sys_login_log` VALUES (456, 'bloguser', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2023-03-01 15:58:26');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '父菜单ID',
  `menu_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `menu_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类别(目录，菜单，按钮)',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `perms` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `is_frame` int(11) NULL DEFAULT 0 COMMENT '是否为外链（0否 1是）',
  `menu_sort` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '菜单状态(1显示 0隐藏)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否删除(0代表存在 1代表删除)',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '主页', 'menu', 'el-icon-s-home', '/index', '/Index', NULL, 0, 1, '1', '管理员创建', '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-07 14:32:08');
INSERT INTO `sys_menu` VALUES (2, 0, '系统管理', 'directory', 'el-icon-setting', '', NULL, NULL, 0, 2, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-07 15:11:29');
INSERT INTO `sys_menu` VALUES (3, 2, '用户管理', 'menu', 'el-icon-user', '/system/sysUser', '/system/sysUser', NULL, 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-07 15:11:42');
INSERT INTO `sys_menu` VALUES (4, 2, '角色管理', 'menu', 'el-icon-user-solid', '/system/sysRole', '/system/sysRole', NULL, 0, 2, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-07 15:11:47');
INSERT INTO `sys_menu` VALUES (5, 2, '菜单管理', 'menu', 'el-icon-s-grid', '/system/sysMenu', '/system/sysMenu', NULL, 0, 3, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-07 15:13:01');
INSERT INTO `sys_menu` VALUES (7, 5, '新增菜单', 'button', '#', '', NULL, 'system:sysMenu:add', 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-07 12:34:28');
INSERT INTO `sys_menu` VALUES (8, 5, '编辑菜单', 'button', '#', '', NULL, 'system:sysMenu:update', 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-11 14:03:28');
INSERT INTO `sys_menu` VALUES (9, 5, '查询菜单', 'button', '#', '', NULL, 'system:sysMenu:query', 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-07 13:38:15');
INSERT INTO `sys_menu` VALUES (10, 5, '删除菜单', 'button', '#', '', NULL, 'system:sysMenu:delete', 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-07 12:34:35');
INSERT INTO `sys_menu` VALUES (13, 4, '新增角色', 'button', '#', '', NULL, 'system:sysRole:add', 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-07 12:32:47');
INSERT INTO `sys_menu` VALUES (14, 4, '编辑角色', 'button', '#', '', NULL, 'system:sysRole:update', 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-11 14:03:19');
INSERT INTO `sys_menu` VALUES (15, 4, '查询角色', 'button', '#', '', NULL, 'system:sysRole:query', 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-07 13:38:05');
INSERT INTO `sys_menu` VALUES (16, 4, '删除角色', 'button', '#', '', NULL, 'system:sysRole:delete', 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-07 12:33:49');
INSERT INTO `sys_menu` VALUES (17, 0, '深度学习', 'directory', 'el-icon-camera-solid', '', NULL, NULL, 0, 5, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-13 16:04:17');
INSERT INTO `sys_menu` VALUES (18, 4, '分配菜单', 'button', '#', '', NULL, 'system:sysRole:permission', 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-07 12:33:47');
INSERT INTO `sys_menu` VALUES (19, 0, '博客子系统', 'directory', 'el-icon-eleme', '', NULL, NULL, 0, 6, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2023-01-27 09:44:31');
INSERT INTO `sys_menu` VALUES (20, 3, '新增用户', 'button', '#', '', NULL, 'system:sysUser:add', 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:50:35', 'jingwen', '2022-09-07 12:34:04');
INSERT INTO `sys_menu` VALUES (21, 3, '编辑用户', 'button', '#', '', NULL, 'system:sysUser:update', 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:51:03', 'jingwen', '2022-09-11 14:03:10');
INSERT INTO `sys_menu` VALUES (22, 3, '删除用户', 'button', '#', '', NULL, 'system:sysUser:delete', 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:51:46', 'jingwen', '2022-09-07 12:34:12');
INSERT INTO `sys_menu` VALUES (23, 3, '查询用户', 'button', '#', '', NULL, 'system:sysUser:query', 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:52:14', 'jingwen', '2022-09-07 12:34:07');
INSERT INTO `sys_menu` VALUES (24, 3, '分配角色', 'button', '#', '', NULL, 'system:sysUser:editRole', 0, 1, '1', NULL, '0', 'admin', '2022-09-06 16:52:35', 'jingwen', '2022-09-07 12:34:10');
INSERT INTO `sys_menu` VALUES (25, 2, '系统日志', 'directory', 'el-icon-date', '', '', '', 0, 4, '1', '系统日志', '0', 'jingwen', '2022-09-06 21:00:12', 'jingwen', '2022-09-07 15:13:18');
INSERT INTO `sys_menu` VALUES (30, 25, '操作日志', 'menu', 'el-icon-tickets', '/system/sysOperationLog', '/system/sysOperationLog', '', 0, 1, '1', '操作日志', '0', 'jingwen', '2022-09-07 11:53:02', 'jingwen', '2022-09-11 12:19:01');
INSERT INTO `sys_menu` VALUES (31, 30, '删除日志', 'button', '', '', '', 'system:sysOperationLog:delete', 0, 1, '1', '', '0', 'jingwen', '2022-09-07 11:53:25', 'jingwen', '2022-09-11 14:12:17');
INSERT INTO `sys_menu` VALUES (32, 3, '重置密码', 'button', '', '', '', 'system:sysUser:resetPassword', 0, 1, '1', '', '0', 'jingwen', '2022-09-10 20:56:12', 'jingwen', '2022-09-10 20:56:12');
INSERT INTO `sys_menu` VALUES (36, 30, '查询日志', 'button', '', '', '', 'system:sysOperationLog:query', 0, 1, '1', '查看日志详情', '0', 'jingwen', '2022-09-11 14:13:38', 'admin', '2022-09-11 20:46:48');
INSERT INTO `sys_menu` VALUES (37, 25, '登录日志', 'menu', 'el-icon-document', '/system/sysLoginLog', '/system/sysLoginLog', '', 0, 2, '1', '', '0', 'jingwen', '2022-09-11 18:36:54', 'jingwen', '2022-09-11 20:52:58');
INSERT INTO `sys_menu` VALUES (38, 37, '查询日志', 'button', '', '', '', 'system:sysLoginLog:query', 0, 1, '1', '', '0', 'jingwen', '2022-09-11 18:37:43', 'admin', '2022-09-11 20:47:04');
INSERT INTO `sys_menu` VALUES (39, 37, '删除日志', 'button', '', '', '', 'system:sysLoginLog:delete', 0, 1, '1', '', '0', 'jingwen', '2022-09-11 18:38:07', 'jingwen', '2022-09-11 18:38:07');
INSERT INTO `sys_menu` VALUES (40, 17, '人脸检测', 'directory', 'el-icon-s-custom', '', '', '', 0, 1, '1', '', '0', 'jingwen', '2022-09-13 16:28:31', 'jingwen', '2022-09-13 16:29:55');
INSERT INTO `sys_menu` VALUES (41, 40, '人脸库管理', 'menu', 'el-icon-takeaway-box', '/deeplearning/dlFaceDatabase', '/deeplearning/dlFaceDatabase', '', 0, 1, '1', '', '0', 'jingwen', '2022-09-13 16:30:35', 'jingwen', '2022-09-13 21:17:54');
INSERT INTO `sys_menu` VALUES (42, 40, '文件检测', 'menu', 'el-icon-folder-add', '/deeplearning/dlFaceDetectFile', '/deeplearning/dlFaceDetectFile', '', 0, 2, '1', '', '0', 'jingwen', '2022-09-13 16:30:52', 'jingwen', '2022-09-22 19:00:40');
INSERT INTO `sys_menu` VALUES (44, 19, '文章管理', 'directory', 'el-icon-collection', '', '', '', 0, 1, '1', '', '0', 'jingwen', '2023-01-08 23:19:10', 'jingwen', '2023-01-08 23:19:10');
INSERT INTO `sys_menu` VALUES (46, 44, '文章列表', 'menu', 'el-icon-document-copy', '/blog/blogArticle', '/blog/blogArticle', '', 0, 4, '1', '', '0', 'jingwen', '2023-01-08 23:20:16', 'jingwen', '2023-01-25 13:41:57');
INSERT INTO `sys_menu` VALUES (47, 19, '类别管理', 'menu', 'el-icon-document-checked', '/blog/blogCategory', '/blog/blogCategory', '', 0, 2, '1', '', '0', 'jingwen', '2023-01-08 23:21:47', 'jingwen', '2023-02-28 12:19:31');
INSERT INTO `sys_menu` VALUES (48, 19, '标签管理', 'menu', 'el-icon-files', '/blog/blogTag', '/blog/blogTag', '', 0, 3, '1', '', '0', 'jingwen', '2023-01-08 23:21:59', 'jingwen', '2023-02-28 12:22:06');
INSERT INTO `sys_menu` VALUES (49, 19, '评论管理', 'menu', 'el-icon-chat-line-round', '/blog/blogComment', '/blog/blogComment', '', 0, 4, '1', '', '0', 'jingwen', '2023-01-08 23:22:40', 'jingwen', '2023-02-28 12:19:44');
INSERT INTO `sys_menu` VALUES (52, 44, '新增文章', 'menu', 'el-icon-zoom-in', '/blog/blogAddArticle', '/blog/blogAddArticle', '', 0, 1, '1', '', '0', 'jingwen', '2023-01-25 13:41:23', 'jingwen', '2023-01-25 18:59:56');
INSERT INTO `sys_menu` VALUES (53, 44, '编辑文章', 'menu', 'el-icon-edit', '/blog/blogUpdateArticle/**', '/blog/blogUpdateArticle', '', 0, 2, '0', '', '0', 'jingwen', '2023-01-25 19:00:40', 'jingwen', '2023-01-25 19:40:48');
INSERT INTO `sys_menu` VALUES (54, 46, '文章顶置', 'button', '', '', '', 'blog:blogArticle:editArticleTop', 0, 1, '1', '', '0', 'jingwen', '2023-01-26 13:03:40', 'jingwen', '2023-01-26 13:03:40');
INSERT INTO `sys_menu` VALUES (55, 46, '编辑文章', 'button', '', '', '', 'blog:blogArticle:editArticle', 0, 1, '1', '', '0', 'jingwen', '2023-01-26 15:33:36', 'jingwen', '2023-01-26 15:33:36');
INSERT INTO `sys_menu` VALUES (56, 46, '删除文章', 'button', '', '', '', 'blog:blogArticle:deleteArticle', 0, 1, '1', '', '0', 'jingwen', '2023-01-26 15:34:02', 'jingwen', '2023-01-26 15:34:02');
INSERT INTO `sys_menu` VALUES (57, 46, '审核文章', 'button', '', '', '', 'blog:blogArticle:checkArticle', 0, 1, '1', '', '0', 'jingwen', '2023-01-26 17:20:03', 'jingwen', '2023-01-26 17:20:33');
INSERT INTO `sys_menu` VALUES (58, 19, '留言板管理', 'menu', 'el-icon-s-platform', '/blog/blogMessage', '/blog/blogMessage', '', 0, 5, '1', '', '0', 'jingwen', '2023-02-28 12:21:03', 'jingwen', '2023-02-28 12:21:11');
INSERT INTO `sys_menu` VALUES (59, 19, '朋友圈管理', 'menu', 'el-icon-s-custom', '/blog/blogMoments', '/blog/blogMoments', '', 0, 7, '1', '', '0', 'jingwen', '2023-02-28 12:22:04', 'jingwen', '2023-02-28 18:39:11');
INSERT INTO `sys_menu` VALUES (60, 19, '友链管理', 'menu', 'el-icon-share', '/blog/blogFriend', '/blog/blogFriend', '', 0, 6, '1', '', '0', 'jingwen', '2023-02-28 12:23:19', 'jingwen', '2023-02-28 18:39:15');
INSERT INTO `sys_menu` VALUES (61, 19, '网站配置', 'menu', 'el-icon-s-marketing', '/blog/blogWeb', '/blog/blogWeb', '', 0, 9, '1', '', '0', 'jingwen', '2023-03-01 14:08:01', 'jingwen', '2023-03-01 14:08:01');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '操作日志ID',
  `opt_module` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作模块名',
  `opt_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作类型',
  `opt_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作描述',
  `request_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求url',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求方式',
  `opt_method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `response_result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '响应参数',
  `status` int(11) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '错误信息',
  `opt_user` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作人',
  `opt_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作ip',
  `opt_location` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作地址',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1045 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (1128, '人脸库模块', '删除', '删除人脸库', '/dlFaceDatabase/deleteBatch', 'POST', 'com.jw_server.controller.deeplearning.DlFaceDatabaseController.deleteBatch', '[[34]]', '{\"code\":200,\"msg\":\"\"}', 0, NULL, 'jingwen', '127.0.0.1', '内网IP', '2023-03-01 15:54:12');
INSERT INTO `sys_operation_log` VALUES (1129, '文件上传模块', '上传', '上传文件', '/file/fileUpload/blog/comments', 'POST', 'com.jw_server.controller.FileUploadController.fileUpload', NULL, '{\"code\":200,\"data\":\"/static/upload/blog/comments/2023-03-01-809406f730d74791868afd632bafbcda.jpeg\",\"msg\":\"\"}', 0, NULL, 'bloguser', '127.0.0.1', '内网IP', '2023-03-01 15:59:30');
INSERT INTO `sys_operation_log` VALUES (1130, '博客文章评论模块', '新增', '前台新增一条评论', '/blogComment/front/addComment', 'POST', 'com.jw_server.controller.blog.BlogCommentController.addComment', '[{\"commentCheck\":\"1\",\"articleId\":38,\"commentType\":\"article\",\"commentContent\":\"评论测试[default:衰][ppx:xiaochou@3x]\\n<bloguser,/static/upload/blog/comments/2023-03-01-809406f730d74791868afd632bafbcda.jpeg>\",\"userId\":24}]', '{\"code\":200,\"msg\":\"\"}', 0, NULL, 'bloguser', '127.0.0.1', '内网IP', '2023-03-01 15:59:35');
INSERT INTO `sys_operation_log` VALUES (1131, '博客文章评论模块', '新增', '前台新增一条评论', '/blogComment/front/addComment', 'POST', 'com.jw_server.controller.blog.BlogCommentController.addComment', '[{\"floorCommentId\":155,\"replyUserId\":24,\"commentCheck\":\"1\",\"replyCommentId\":155,\"articleId\":38,\"commentType\":\"article\",\"commentContent\":\"回复测试[default:摸鱼]\",\"userId\":24}]', '{\"code\":200,\"msg\":\"\"}', 0, NULL, 'bloguser', '127.0.0.1', '内网IP', '2023-03-01 15:59:53');
INSERT INTO `sys_operation_log` VALUES (1132, '博客留言模块', '新增', '前台新增一条留言', '/blogMessage/front/addMessage', 'POST', 'com.jw_server.controller.blog.BlogMessageController.add', '[{\"messageEmail\":\"bloguser@qq.com\",\"createTime\":\"2023-03-01T16:00:22.812596500\",\"messageCheck\":\"1\",\"messageId\":7,\"messageAvatar\":\"/static/upload/avatar/2023-02-26-73cfdb278f9e44ffb3519ae92107d59d.jpg\",\"messageNickname\":\"博客用户\",\"messageContent\":\"留言板测试\"}]', '{\"code\":200,\"msg\":\"\"}', 0, NULL, 'bloguser', '127.0.0.1', '内网IP', '2023-03-01 16:00:23');
INSERT INTO `sys_operation_log` VALUES (1133, '博客文章评论模块', '新增', '前台新增一条评论', '/blogComment/front/addComment', 'POST', 'com.jw_server.controller.blog.BlogCommentController.addComment', '[{\"commentCheck\":\"1\",\"articleId\":0,\"commentType\":\"message\",\"commentContent\":\"留言评论测试[ppx:nianyu@3x]\",\"userId\":24}]', '{\"code\":200,\"msg\":\"\"}', 0, NULL, 'bloguser', '127.0.0.1', '内网IP', '2023-03-01 16:00:41');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色标识',
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名',
  `role_sort` int(11) NULL DEFAULT 0,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '状态(1正常 0停用)',
  `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否删除(0代表存在 1代表删除)',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (6, 'admin', '管理员', 0, '管理员', '1', '0', '123', '2022-08-31 18:45:24', 'jingwen', '2022-09-06 20:30:10');
INSERT INTO `sys_role` VALUES (15, 'DL_ROLE', '深度学习角色', 0, '深度学习专用角色', '1', '0', 'jingwen', '2022-09-13 16:03:28', 'jingwen', '2022-09-13 16:03:28');
INSERT INTO `sys_role` VALUES (16, 'SHOW_ROLE', '演示角色', 0, '演示角色', '1', '0', 'jingwen', '2022-09-27 12:21:45', 'jingwen', '2023-01-13 20:38:39');
INSERT INTO `sys_role` VALUES (17, 'BLOG_REGISTER_ROLE', '博客初始用户', 0, '博客初始用户，用户注册时自动分配，请勿删除', '1', '0', 'jingwen', '2023-02-26 23:52:58', 'jingwen', '2023-02-26 23:52:58');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (6, 1);
INSERT INTO `sys_role_menu` VALUES (6, 2);
INSERT INTO `sys_role_menu` VALUES (6, 3);
INSERT INTO `sys_role_menu` VALUES (6, 4);
INSERT INTO `sys_role_menu` VALUES (6, 5);
INSERT INTO `sys_role_menu` VALUES (6, 7);
INSERT INTO `sys_role_menu` VALUES (6, 8);
INSERT INTO `sys_role_menu` VALUES (6, 9);
INSERT INTO `sys_role_menu` VALUES (6, 10);
INSERT INTO `sys_role_menu` VALUES (6, 13);
INSERT INTO `sys_role_menu` VALUES (6, 14);
INSERT INTO `sys_role_menu` VALUES (6, 15);
INSERT INTO `sys_role_menu` VALUES (6, 16);
INSERT INTO `sys_role_menu` VALUES (6, 17);
INSERT INTO `sys_role_menu` VALUES (6, 18);
INSERT INTO `sys_role_menu` VALUES (6, 19);
INSERT INTO `sys_role_menu` VALUES (6, 20);
INSERT INTO `sys_role_menu` VALUES (6, 21);
INSERT INTO `sys_role_menu` VALUES (6, 22);
INSERT INTO `sys_role_menu` VALUES (6, 23);
INSERT INTO `sys_role_menu` VALUES (6, 24);
INSERT INTO `sys_role_menu` VALUES (6, 25);
INSERT INTO `sys_role_menu` VALUES (6, 30);
INSERT INTO `sys_role_menu` VALUES (6, 31);
INSERT INTO `sys_role_menu` VALUES (6, 32);
INSERT INTO `sys_role_menu` VALUES (6, 36);
INSERT INTO `sys_role_menu` VALUES (6, 37);
INSERT INTO `sys_role_menu` VALUES (6, 38);
INSERT INTO `sys_role_menu` VALUES (6, 39);
INSERT INTO `sys_role_menu` VALUES (6, 40);
INSERT INTO `sys_role_menu` VALUES (6, 41);
INSERT INTO `sys_role_menu` VALUES (6, 42);
INSERT INTO `sys_role_menu` VALUES (6, 44);
INSERT INTO `sys_role_menu` VALUES (6, 46);
INSERT INTO `sys_role_menu` VALUES (6, 47);
INSERT INTO `sys_role_menu` VALUES (6, 48);
INSERT INTO `sys_role_menu` VALUES (6, 49);
INSERT INTO `sys_role_menu` VALUES (6, 52);
INSERT INTO `sys_role_menu` VALUES (6, 53);
INSERT INTO `sys_role_menu` VALUES (6, 54);
INSERT INTO `sys_role_menu` VALUES (6, 55);
INSERT INTO `sys_role_menu` VALUES (6, 56);
INSERT INTO `sys_role_menu` VALUES (6, 57);
INSERT INTO `sys_role_menu` VALUES (6, 58);
INSERT INTO `sys_role_menu` VALUES (6, 59);
INSERT INTO `sys_role_menu` VALUES (6, 60);
INSERT INTO `sys_role_menu` VALUES (6, 61);
INSERT INTO `sys_role_menu` VALUES (15, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2);
INSERT INTO `sys_role_menu` VALUES (15, 17);
INSERT INTO `sys_role_menu` VALUES (15, 25);
INSERT INTO `sys_role_menu` VALUES (15, 30);
INSERT INTO `sys_role_menu` VALUES (15, 31);
INSERT INTO `sys_role_menu` VALUES (15, 36);
INSERT INTO `sys_role_menu` VALUES (15, 37);
INSERT INTO `sys_role_menu` VALUES (15, 38);
INSERT INTO `sys_role_menu` VALUES (15, 39);
INSERT INTO `sys_role_menu` VALUES (15, 40);
INSERT INTO `sys_role_menu` VALUES (15, 41);
INSERT INTO `sys_role_menu` VALUES (15, 42);
INSERT INTO `sys_role_menu` VALUES (16, 1);
INSERT INTO `sys_role_menu` VALUES (16, 2);
INSERT INTO `sys_role_menu` VALUES (16, 3);
INSERT INTO `sys_role_menu` VALUES (16, 4);
INSERT INTO `sys_role_menu` VALUES (16, 5);
INSERT INTO `sys_role_menu` VALUES (16, 9);
INSERT INTO `sys_role_menu` VALUES (16, 15);
INSERT INTO `sys_role_menu` VALUES (16, 17);
INSERT INTO `sys_role_menu` VALUES (16, 19);
INSERT INTO `sys_role_menu` VALUES (16, 23);
INSERT INTO `sys_role_menu` VALUES (16, 25);
INSERT INTO `sys_role_menu` VALUES (16, 30);
INSERT INTO `sys_role_menu` VALUES (16, 36);
INSERT INTO `sys_role_menu` VALUES (16, 37);
INSERT INTO `sys_role_menu` VALUES (16, 38);
INSERT INTO `sys_role_menu` VALUES (16, 40);
INSERT INTO `sys_role_menu` VALUES (16, 41);
INSERT INTO `sys_role_menu` VALUES (16, 42);
INSERT INTO `sys_role_menu` VALUES (16, 44);
INSERT INTO `sys_role_menu` VALUES (16, 46);
INSERT INTO `sys_role_menu` VALUES (16, 47);
INSERT INTO `sys_role_menu` VALUES (16, 48);
INSERT INTO `sys_role_menu` VALUES (16, 49);
INSERT INTO `sys_role_menu` VALUES (16, 52);
INSERT INTO `sys_role_menu` VALUES (16, 53);
INSERT INTO `sys_role_menu` VALUES (16, 58);
INSERT INTO `sys_role_menu` VALUES (16, 59);
INSERT INTO `sys_role_menu` VALUES (16, 60);
INSERT INTO `sys_role_menu` VALUES (16, 61);
INSERT INTO `sys_role_menu` VALUES (17, 1);
INSERT INTO `sys_role_menu` VALUES (17, 2);
INSERT INTO `sys_role_menu` VALUES (17, 3);
INSERT INTO `sys_role_menu` VALUES (17, 4);
INSERT INTO `sys_role_menu` VALUES (17, 5);
INSERT INTO `sys_role_menu` VALUES (17, 9);
INSERT INTO `sys_role_menu` VALUES (17, 15);
INSERT INTO `sys_role_menu` VALUES (17, 19);
INSERT INTO `sys_role_menu` VALUES (17, 23);
INSERT INTO `sys_role_menu` VALUES (17, 25);
INSERT INTO `sys_role_menu` VALUES (17, 30);
INSERT INTO `sys_role_menu` VALUES (17, 36);
INSERT INTO `sys_role_menu` VALUES (17, 37);
INSERT INTO `sys_role_menu` VALUES (17, 38);
INSERT INTO `sys_role_menu` VALUES (17, 44);
INSERT INTO `sys_role_menu` VALUES (17, 46);
INSERT INTO `sys_role_menu` VALUES (17, 47);
INSERT INTO `sys_role_menu` VALUES (17, 48);
INSERT INTO `sys_role_menu` VALUES (17, 49);
INSERT INTO `sys_role_menu` VALUES (17, 52);
INSERT INTO `sys_role_menu` VALUES (17, 53);
INSERT INTO `sys_role_menu` VALUES (17, 54);
INSERT INTO `sys_role_menu` VALUES (17, 55);
INSERT INTO `sys_role_menu` VALUES (17, 56);
INSERT INTO `sys_role_menu` VALUES (17, 57);
INSERT INTO `sys_role_menu` VALUES (17, 58);
INSERT INTO `sys_role_menu` VALUES (17, 59);
INSERT INTO `sys_role_menu` VALUES (17, 60);
INSERT INTO `sys_role_menu` VALUES (17, 61);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '密码',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户昵称',
  `user_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户类型（模块名）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '保密' COMMENT '用户性别',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像地址',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '状态(1正常 0停用)',
  `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否删除(0代表存在 1代表删除)',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (7, NULL, 'admin', '$2a$10$CFcSWC9cIEQZIjGC3tCWrOjBifdaER3LOyi9PfkAJRludP3b.sZMy', '系统管理员', 'admin', 'admin@admin.com', '15644584597', '男', '/static/upload/avatar/2022-09-22-3d87428a6e4e553c6875d58c593f7c7f.png', '系统管理员,jingwen创建，请勿随便编辑', '2023-02-20 18:46:33', '1', '0', '123', '2022-09-06 14:41:00', 'admin', '2022-09-22 16:28:03');
INSERT INTO `sys_user` VALUES (8, NULL, 'jingwen', '$2a$10$2bffMPbMd6uUINpEO39Eaev9v3BQpsStQx/Bni26SJOfZ1PIKxRj6', '靖文', 'admin', 'jingwen@qq.com', '17965788459', '保密', '/static/upload/avatar/2023-02-04-41038e3739eb4e3689f6acf5d203c40b.jpeg', '靖文235', '2023-03-01 15:48:12', '1', '0', 'admin', '2022-09-06 16:28:54', 'jingwen', '2023-02-06 17:27:40');
INSERT INTO `sys_user` VALUES (14, NULL, 'dluser', '$2a$10$z9cxaj57nM0wVBnG.DqtcONvgojE9vJi0B7mRWi/UcacwD/4OoRtq', '深度学习专用角色', 'DL_ROLE', 'dluser@qq.com', '15845912368', '男', '/static/upload/avatar/2022-09-27-c26ab417430847058b3c1f9bf705009e.png', '深度学习专用角色', '2022-09-27 11:12:03', '1', '0', 'unknown', '2022-09-13 16:07:48', 'dluser', '2022-09-27 11:12:17');
INSERT INTO `sys_user` VALUES (15, NULL, 'showuser', '$2a$10$FLOC4qXZ4rKnXAR8ozrstuC2T5aBEfGYwswJlmWk4pVe8N.MzX9WS', '演示用户', 'SHOW_ROLE', 'showuser@qq.com', '13345678910', '男', '/static/upload/avatar/2023-03-01-d16a20a0c9e143f098cbf18c23c4ceda.png', '', '2023-03-01 15:45:11', '1', '0', 'jingwen', '2022-09-27 12:21:04', '演示用户', '2023-03-01 15:48:07');
INSERT INTO `sys_user` VALUES (24, NULL, 'bloguser', '$2a$10$kQkv4innDKgqwkRzbb8anODQHIm5F1DvMDezMHlUugmWZuzK0es4O', '博客用户', 'BLOG_REGISTER_ROLE', 'bloguser@qq.com', '', '男', '/static/upload/avatar/2023-02-26-73cfdb278f9e44ffb3519ae92107d59d.jpg', NULL, '2023-03-01 15:58:26', '1', '0', 'unknown', '2023-02-26 14:20:30', 'jingwen2', '2023-02-26 16:25:20');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (7, 6);
INSERT INTO `sys_user_role` VALUES (8, 6);
INSERT INTO `sys_user_role` VALUES (8, 15);
INSERT INTO `sys_user_role` VALUES (14, 15);
INSERT INTO `sys_user_role` VALUES (15, 16);
INSERT INTO `sys_user_role` VALUES (24, 17);
INSERT INTO `sys_user_role` VALUES (30, 17);
INSERT INTO `sys_user_role` VALUES (32, 16);
INSERT INTO `sys_user_role` VALUES (34, 16);
INSERT INTO `sys_user_role` VALUES (35, 17);
INSERT INTO `sys_user_role` VALUES (36, 16);

SET FOREIGN_KEY_CHECKS = 1;
