if not redis.call('get', KEYS[1]) then
    return nil
else
	return redis.call('incrby', KEYS[1], ARGV[1])
end