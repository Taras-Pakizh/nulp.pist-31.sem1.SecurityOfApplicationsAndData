package ua.feo.md5

static void main(String[] args) {
    def res
    if (args.length == 0) {
        res = "abcsite"
    } else {
        res = args[0]
    }
    println new MD5().get_md5(res)
}
