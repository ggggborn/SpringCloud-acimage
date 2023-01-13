import request from '../utils/requestx';

export const fetchData = () => {
    return request({
        url: './table.json',
        method: 'get'
    });
};
