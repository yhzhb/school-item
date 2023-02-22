#ifndef _TYPES_H_
#define _TYPES_H_

#define MAX_NAME_LEN    128     
typedef int          boolean;
typedef enum newfs_file_type {
    NEWFS_REG_FILE,    
    NEWFS_DIR    //目录文件
} NEWFS_FILE_TYPE;
/******************************************************************************
* SECTION: Macro
*******************************************************************************/
#define TRUE                    1
#define FALSE                   0
#define UINT32_BITS             32
#define UINT8_BITS              8

#define NEWFS_MAGIC_NUM           0x52415453  
#define NEWFS_SUPER_OFS           0
#define NEWFS_ROOT_INO            0



#define NEWFS_ERROR_NONE          0
#define NEWFS_ERROR_ACCESS        EACCES
#define NEWFS_ERROR_SEEK          ESPIPE     
#define NEWFS_ERROR_ISDIR         EISDIR
#define NEWFS_ERROR_NOSPACE       ENOSPC
#define NEWFS_ERROR_EXISTS        EEXIST
#define NEWFS_ERROR_NOTFOUND      ENOENT
#define NEWFS_ERROR_UNSUPPORTED   ENXIO
#define NEWFS_ERROR_IO            EIO     /* Error Input/Output */
#define NEWFS_ERROR_INVAL         EINVAL  /* Invalid Args */

#define NEWFS_MAX_FILE_NAME       128
#define NEWFS_INODE_PER_FILE      1
#define NEWFS_DATA_PER_FILE       6
#define NEWFS_DEFAULT_PERM        0777

#define NEWFS_IOC_MAGIC           'S'
#define NEWFS_IOC_SEEK            _IO(NEWFS_IOC_MAGIC, 0)

#define NEWFS_FLAG_BUF_DIRTY      0x1
#define NEWFS_FLAG_BUF_OCCUPY     0x2

//#define DENTRY_PER_DATA_BLOCK     NEWFS_IO_SZ()/sizeof(struct newfs_dentry)
/******************************************************************************
* SECTION: Macro Function
*******************************************************************************/
#define NEWFS_IO_SZ()                     (newfs_super.sz_io)
#define NEWFS_BLOCK_SZ()                  (newfs_super.sz_io * 2)
#define NEWFS_DISK_SZ()                   (newfs_super.sz_disk)
#define NEWFS_DRIVER()                    (newfs_super.driver_fd)

#define NEWFS_ROUND_DOWN(value, round)    (value % round == 0 ? value : (value / round) * round)
#define NEWFS_ROUND_UP(value, round)      (value % round == 0 ? value : (value / round + 1) * round)

#define NEWFS_BLKS_SZ(blks)               (blks * NEWFS_BLOCK_SZ())//控制偏移
// (blks * NEWFS_IO_SZ())
#define NEWFS_ASSIGN_FNAME(psfs_dentry, _name)\ 
                                          memcpy(psfs_dentry->name, _name, strlen(_name))
#define NEWFS_INO_OFS(ino)                (newfs_super.map_data_offset +  NEWFS_BLKS_SZ(newfs_super.map_data_blks)+ ino*sizeof(struct newfs_inode_d)) 
#define NEWFS_DATA_OFS(ino)               (NEWFS_INO_OFS(newfs_super.max_ino) + NEWFS_BLKS_SZ(ino))
#define NEWFS_INODES_SZ(ino)              (ino*sizeof(struct newfs_inode_d))
#define NEWFS_IS_DIR(pinode)              (pinode->dentry->ftype == NEWFS_DIR)
#define NEWFS_IS_REG(pinode)              (pinode->dentry->ftype == NEWFS_REG_FILE)

/******************************************************************************
* SECTION: FS Specific Structure - In memory structure
*******************************************************************************/
struct newfs_dentry;
struct newfs_inode;
struct newfs_super;

struct custom_options {
	const char*        device;
};

struct newfs_super {
    uint32_t magic;      //幻数
    //int      fd;
    int      driver_fd;
    
    int                sz_io;     //一次I/O的数据大小
    int                sz_disk;   //磁盘大小
    int                sz_usage;
    
    int                max_ino;   //最多支持的文件数
    uint8_t*           map_inode; //索引节点位图
    int                map_inode_blks; //索引节点位图所占用的块数
    int                map_inode_offset; //索引节点位图在磁盘上的偏移

    int                max_data;//最多支持的数据块数
    uint8_t*           map_data; //数据位图
    int                map_data_blks; //数据位图所占用的块数
    int                map_data_offset;//数据位图在磁盘上的偏移

    int                inode_offset;
    int                data_offset;

    boolean            is_mounted;  //判断是否挂载
    struct newfs_dentry* root_dentry;  //根目录
};
struct newfs_inode
{
    uint32_t           ino;     // 在inode位图中的下标                      
    int                size;    /* 文件已占用空间 */
    int                dir_cnt; //如果是目录类型文件，下面有几个目录项
    struct newfs_dentry* dentry;   /* 指向该inode的dentry */
    struct newfs_dentry* dentrys;   /* 所有目录项 */
    uint8_t*         data;//实际要有建议通过指针数组实现
    //uint8_t*           data[NEWFS_DATA_PER_FILE]; //可能用不上，因为没有添加读写操作
    char               target_path[NEWFS_MAX_FILE_NAME];/* store traget path when it is a symlink */

    int                link;
    uint8_t              block_pointer[NEWFS_DATA_PER_FILE];      //数据块号数组     
};  

struct newfs_dentry
{
    char                name[MAX_NAME_LEN]; // 指向的ino文件名
    struct newfs_dentry* parent;            /* 父亲Inode的dentry */
    struct newfs_dentry* brother;           /* 兄弟 */
    uint32_t             ino;
    struct newfs_inode*  inode;            /* 指向inode */
    NEWFS_FILE_TYPE      ftype;
    int                  valid;            // 该目录项是否有效
};

static inline struct newfs_dentry* new_dentry(char * fname, NEWFS_FILE_TYPE ftype) {
    struct newfs_dentry * dentry = (struct newfs_dentry *)malloc(sizeof(struct newfs_dentry));
    //struct newfs_dentry * dentry;
    memset(dentry, 0, sizeof(struct newfs_dentry));
    NEWFS_ASSIGN_FNAME(dentry,fname);
    dentry->ftype   = ftype;
    dentry->ino     = -1;
    dentry->inode   = NULL;
    dentry->parent  = NULL;
    dentry->brother = NULL;                                            
}

/******************************************************************************
* SECTION: FS Specific Structure - Disk structure
*******************************************************************************/
struct newfs_super_d
{
    uint32_t           magic_num; //幻数
    int                sz_usage;
    
    int                max_ino; //最多支持文件数
    
    int                map_inode_blks;
    int                map_inode_offset;

    int                max_data;
    int                map_data_blks;
    int                map_data_offset;

    int                inode_offset;       //inode节点偏移量
    int                data_offset;        //数据块偏移量
};

struct newfs_inode_d
{
    int                ino;                /* 在inode位图中的下标 */
    int                size;               /* 文件已占用空间 */
    int               link;
    int                dir_cnt;            //如果是目录类型文件，下面有几个目录项
    NEWFS_FILE_TYPE      ftype;  
    uint8_t              block_pointer[NEWFS_DATA_PER_FILE];       //数据块指针 
    char               target_path[NEWFS_MAX_FILE_NAME];
};  

struct newfs_dentry_d
{
    char               fname[MAX_NAME_LEN];
    NEWFS_FILE_TYPE      ftype;
    int                valid;                          //该目录项是否有效
    int                ino;                           /* 指向的ino号 */
};  
#endif /* _TYPES_H_ */