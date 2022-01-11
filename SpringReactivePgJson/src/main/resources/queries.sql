/* Find by product name */
SELECT *  FROM products
WHERE infos ->> 'name' = 'XBOX';

/* Find distributors (return as json) of a product name */
SELECT json_array_elements(infos -> 'distributors')
FROM products
WHERE infos ->> 'name' = 'XBOX';


/* Find distributors name's of a product name */
SELECT
        json_array_elements(infos -> 'distributors') ->> 'name' as name,
    (json_array_elements(infos -> 'distributors') -> 'cities' ) as cities
FROM products
WHERE infos ->> 'name' = 'XBOX';

/* Find cities where a product is distibuted */
SELECT json_array_elements(json_array_elements(infos -> 'distributors') -> 'cities') as city
FROM products
WHERE infos ->> 'name' = 'XBOX';


/* Find products distribued in a city */
SELECT q.product ->> 'name' as name,
    q.product -> 'price' as price,
    q.product -> 'distributors' as distributors
FROM
    (
    SELECT p.infos as product,
    ((json_array_elements(infos -> 'distributors') -> 'cities')::jsonb ?| array['Poitiers', 'Web']) as result
    FROM products p
    ) q
WHERE q.result = true;