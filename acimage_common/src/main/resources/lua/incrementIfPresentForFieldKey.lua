--如果KEYS[1]的hash key ARGV[1]存在，则为它增加ARGV[2]，并返回增加后的值
--否则返回nil
local base = redis.call('hget',KEYS[1],ARGV[1])
if not base then
    return nil
end
return redis.call('hincrby',KEYS[1],ARGV[1],ARGV[2])
