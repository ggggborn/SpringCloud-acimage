if not redis.call('zscore', KEYS[1], ARGV[1]) then
    return nil
else
-- zincrby key increment member
	return redis.call('zincrby', KEYS[1], ARGV[2], ARGV[1])
end