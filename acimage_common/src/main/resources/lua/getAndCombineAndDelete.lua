--取出KEYS[1]并加到KEYS[2]的ARGV[1]上（如果KEYS[2]的AVG[1]存在），删除KEYS[1]
--返回KEYS[1]对应的值
--KEYS[1] string
--KEYS[2] hash
--ARGV[1] fieldKey
local increment = redis.call('get', KEYS[1])
if not increment then
    return nil
end
local base = redis.call('hget',KEYS[2],ARGV[1])
if base then
    redis.call('hincrby',KEYS[2],ARGV[1],increment)
end
redis.call('del',KEYS[1])
return increment
