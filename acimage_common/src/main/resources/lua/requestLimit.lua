--设n是KEYS长度
--ARGV[1]到ARGV[n]次数限制
--ARGV[n+1]到ARGV[2n]过期时间
--ARGV[2n+1]到ARGV[3n]惩罚过期时间
local result={}
local len = #KEYS
if (not len) or ( tonumber(len)==0 ) then
    return result;
end
for i=1,len do
    local res=redis.call('incrby',KEYS[i],1)
    if res==1 then
        --如果是第一次访问该key则设置过期时间
        redis.call('expire',KEYS[i],ARGV[len+i])
    end
    if res==tonumber(ARGV[i])+1 then
        --超过限制则惩罚过期时间
        redis.call('expire',KEYS[i],ARGV[2*len+i])
    end
    result[i]=res
end
return result
