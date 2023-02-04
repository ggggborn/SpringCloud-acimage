--如果KEYS[1]的hash key ARGV[1]存在，则为它增加ARGV[2]，并返回增加后的值
--否则返回nil
local result={}
local num = ARGV[1]
if not num then
    return result;
end
for i=1,num+1 do
    local res=redis.call('incrby',KEYS[i],1)
    if tostring(res)==ARGV[i+1] then
if not base then
    return nil
end
return redis.call('hincrby',KEYS[1],ARGV[1],ARGV[2])
