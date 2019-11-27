# png-business-service-parent
业务服务中台Maven父项目，定义了统一的Spring Cloud版本，Java编译用版本支持1.8

## png-shr-facade
调用SHR的一个封包，所以其他系统调用SHR都通过该facade接入，内部封装SHR认证实施，以及一些SHR内外部接口转换，敏感信息过滤等
